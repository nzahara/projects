/****************************************************
 *													*
 * iSense Vehicle Detection Module Demo Application	*
 * Version: 1.0										*
 * Date: 	26.7.2011								*
 * Targets:	JN5139R1, JN5139R, JN5148				*
 * Author: 	Carsten Buschmann						*
 * 													*
 * Copyright 2011									*
 * ALL RIGHTS RESERVED								*
 * coalesenses GmbH 								*
 * http://www.coalesenses.com						*
 * 													*
 ****************************************************/

#ifdef ISENSE_JENNIC

#include <isense/config.h>
#include <isense/os.h>
#include <isense/application.h>
#include <isense/task.h>
#include <isense/uart.h>
#include <isense/modules/amr_module/amr_module.h>
#include <isense/modules/gateway_module/gateway_module.h>
#include <isense/button_handler.h>
#include <isense/util/ishell_interpreter.h>
#include <isense/data_handlers.h>
#include <isense/util/util.h>
#include <isense/radio.h>
#include <isense/dispatcher.h>

#define INDOOR

#ifdef INDOOR
	//------------------------------------------------------------------------
	// Below the sensor timing configuration is defined:
	// this is a good indoor setting:
	// - sample every 5 ms, and average 4 of these values, put the result to the buffer
	// - like this, each value handed out to the app is averaged over 20 ms, which is one 50 Hertz period
	// - the buffer is handed over to the app when it is completely full (i.e. contains 25 averaged samples
	// - like this, the app will receive 2 buffers/second
	//------------------------------------------------------------------------

	// interval in ms between 2 sensor samples to be taken
	#define SAMPLE_INTERVAL 5
	// number of sensor samples to be averaged to one value handed over to the application
	#define SAMPLE_COUNT 4
	// number of sensor samples taken before buffer is handed over to the app
	// this value must not exceed the maximum buffer size of 25
	#define BUFFER_SIZE 25
	//------------------------------------------------------------------------
#else
	//------------------------------------------------------------------------
	// This is an alternative sensor timing configuration:
	// this is an outdoor setting optimized for low energy consumption:
	// - sample only every 100 ms (no averaging), as this should be fast anought to notice most vehicles,
	//   as a vehicle travelling at 50km/h makes about 14m/s, and the modules range is approx. 5m
	// - the buffer is handed over to the app when it contains 20 samples
	// - like this, the app will receive a buffer every 2 seconds
	//------------------------------------------------------------------------
	// interval in ms between 2 sensor samples to be taken
	#define SAMPLE_INTERVAL 10
	// number of sensor samples to be averaged to one value handed over to the application
	#define SAMPLE_COUNT 1
	// number of sensor samples taken before buffer is handed over to the app
	// this value must not exceed the maximum buffer size of 25
	#define BUFFER_SIZE 20
#endif

// upper and lower ADC values to trigger auto calibration
// if the auto_compensation is enabled, and the average of all buffer
// values for one channel is above  the upper or below the lower threshold
// the calibration procedure will be invoked automatically
#define UPPER_COMPENSATION_TRIGGER 3900
#define LOWER_COMPENSATION_TRIGGER 200

// Detection thresholds
#define UPPER_DETECTION_TRIGGER 3000
#define LOWER_DETECTION_TRIGGER 1000

// constants for identifying different tasks
#define TASK_CALIBRATE 1
#define TASK_SYNC 2

struct time
{
	bool synched;
	uint32 sec;
	uint16 ms;
};


using namespace isense;

//----------------------------------------------------------------------------
class AmrModuleDemoApplication :
	public Application, 
	public Task,
	public UartPacketHandler,
	public BufferDataHandler,
	public AmrAlarmHandler,
	public SleepHandler,
	public Uint8DataHandler,
	public Sender,
	public Receiver
{

public: 
	AmrModuleDemoApplication(Os &os);
 	bool stand_by (void); // Memory held
 	void wake_up (bool memory_held);
	void execute( void* userdata );
	void boot();
	void set_gain(bool high);
	void handle_uart_packet( uint8 type, uint8* buf, uint8 length );
	void handle_buffer_data( BufferData* buf );
	void handle_alarm( uint16 alarm_bits[2] );
	void handle_alarm( Time time );
	void handle_uint8_data(uint8 data);
	void confirm (uint8 state, uint8 tries, isense::Time time);
	void receive (uint8 len, const uint8 * buf, ISENSE_RADIO_ADDR_TYPE src_addr, ISENSE_RADIO_ADDR_TYPE dest_addr, uint16 signal_strength, uint16 signal_quality, uint8 seq_no, uint8 interface, Time rx_time);
	
private:
	//pointer to sensor driver
	AmrModule* amr_;
	// abstraction class for communication with the PC
	IShellInterpreter* isi_;
	// Driver for Core Module
	CoreModule* cm_;
	// enable or disable autocompensation
	bool auto_compensate_;
	uint16 sample_interval_;
	uint16 sample_count_;
	uint16 buffer_size_;

	// detection parameters
	bool veh_detected;
	bool detecting_veh_max;
	bool detecting_veh_min;
	uint16 car_max;
	uint16 car_min;
	time time_stamp;
	bool veh_det_rts;

	// sync
	bool synched;

};

//----------------------------------------------------------------------------
AmrModuleDemoApplication::
AmrModuleDemoApplication(Os &os) :
	Application(os),
	amr_(NULL),
	isi_(NULL),
	cm_(NULL),
	auto_compensate_(true),
	sample_interval_(SAMPLE_INTERVAL),
	sample_count_(SAMPLE_INTERVAL),
	buffer_size_(BUFFER_SIZE)
{
}

//----------------------------------------------------------------------------
// this function is called upon booting the sensor node
void 
AmrModuleDemoApplication::
boot () // Memory held
{
	os_.debug("Booting VDM10 demo application...");
	os_.debug("+----------------------------------------------------------+");
	os_.debug("| Command synopsis (use with iShell Messenger plugin)      |");
	os_.debug("+----------------------------------------------------------+");
	os_.debug("| 05 00          Set gain low                              |");
	os_.debug("| 05 01          Set gain high                             |");
	os_.debug("| 05 02          Calibrate sensor                          |");
	os_.debug("| 05 03 CC II BB Set timing (CC=cnt, II=int., BB buf size) |");
	os_.debug("| 05 06          Start sensor(s)                           |");
	os_.debug("| 05 07          Stop sensor(s)                            |");
	os_.debug("| 05 08 CC       Set automatic calibration, 00=off, 01=on  |");
	os_.debug("+----------------------------------------------------------+");

	// instantiate Core Module (used to flash the LED here to indicate sleep/wake state
	cm_ = new CoreModule(os());
	// turn on LED, as node is currently awake
	cm_->led_on();

	// register as a sleep handler, to receive sleep/wake events
	os().add_sleep_handler(this);

	// Prevent the node from sleeping
	// This is done here only to be ready to receive commands from iShell via the UART
	// It is not required for the operation of the sensor itself
	os().allow_sleep(false);

	// instantiante the AMR module driver, and set yourself as buffer handler and alarm handler
	amr_ = new AmrModule(os(), sample_count_, sample_interval_, buffer_size_);
	// set yourself as buffer handler and alarm handler
	// like this, you will be delivered all sensor readings with alternating buffers
	// through calls of handle_buffer_data
	amr_->set_handler(this);
	// set yourself as alarm handler
	// like this, the app will receive alarm events, indicating that a vehicle was detected
	// in this case, handle_alarm will be called
	// WARNING: the vehicle detection is extremely primitive
	// for real detection, you must implement your own detection based upon the buffer data
//	amr_->set_alarm_handler(this);

	// set yourself as calibration handler
	// like this, the app will receive calibration events, indicating that the calibration
	// of the AMR has ended.
	//amr_->set_calibration_handler(this);

	// instantiate an iShell interpreter, used for sending buffer data to iShell
	// if the "Curve illustrator (single window)" plugin is active, it will display the sensor data
	isi_ = new IShellInterpreter(os());

	// enable uart interrupts, and set app as packet handler for type 5
	// this this, you can use the "Messenger" plugin of iShell to send command to control the app
	// send 05 00 to set the AMR module gain to low
	// send 05 01 to set the AMR module gain to high
	// send 05 02 to calibrate the AMR module
	os().uart(0).enable_interrupt(true);
	os().uart(0).set_packet_handler(05, this);
	
	// enable radio dispatcher
	os().dispatcher().add_receiver(this);

	// enable AMR Module
	amr_->enable();
	// initiate AMR module calibration
	// this must be done after enabling the module,
	// after moving the module, if sensor values go mad, and from time to time
	// to compensate for temperature drift and sensor saturation
	// if the calibration is successful, the sensor should deliver values around 2000
	// after the calibration if not magnetic fields are present
	//os().debug("Enabled sensor, calibrating...");
	//amr_->calibrate();

	// start sensor
	os().debug("Done. Starting sensor.");
	amr_->start();


	veh_detected = false;
	detecting_veh_max = false;
	detecting_veh_min = false;
	veh_det_rts = false;
	synched = false;
	time_stamp.synched = false;
}

//----------------------------------------------------------------------------
// this function is called when the sensor node wakes from sleep mode
void 
AmrModuleDemoApplication::
wake_up (bool memory_held)
{
	// turn on Core Module LED when node wakes
	cm_->led_on();
}

//----------------------------------------------------------------------------
// this function is called when the sensor node goes to sleep
bool 
AmrModuleDemoApplication::
stand_by () // Memory held
{
	// turn off Core Module LED when node goes to sleep
	cm_->led_off();
	return true;
}

//----------------------------------------------------------------------------
// This function is called as a consequence of calling os().add_task(this, NULL);
// here, the sensor is calibrated, after the according command was received via
// UART from iShell (see constructor for details)
void 
AmrModuleDemoApplication::
execute( void* userdata )
{
	if ((uint32)userdata == TASK_CALIBRATE)
	{
		// stop sensor before calibration
		amr_->stop();
		os().debug("Calibrating AMR sensor...");
		amr_->calibrate();
		os().debug("Done. Starting sensor.");
		amr_->start();
	}
	if ((uint32)userdata == TASK_SYNC)
	{
		os().debug("sync");
		time* t = &time_stamp;
		os().radio().send(0x8efc, 12, (uint8*) t, Radio::ISENSE_RADIO_TX_OPTION_NONE, this);
		synched = time_stamp.synched;
	}
}

//----------------------------------------------------------------------------
// this function is called when the AMR module has filled one buffer
// the data is handed over in a BufferData structure
// For details about the BufferData structure, please refer to the API doc
// at www.coalesenses.com/doxygen
void
	AmrModuleDemoApplication::
	handle_buffer_data( BufferData* buf )
{
	// hand over data to iShell to display it the "Curve illustrator (single window)" plugin
	isi_->send_buffer_data(buf, os().id(), sample_count_ * sample_interval_);
	// Here, additional reasoning based upon the buffer data could be done
	//os().debug("Val1 = %d Val2 = %d sec = %d msec = %d", buf->buf[0], buf->buf[1], buf->sec, buf->ms);
	if (!synched)
	{
		time_stamp.sec = buf->sec;
		time_stamp.ms  = buf->ms;
		os_.add_task( this, (void*)TASK_SYNC);
		return;
	}
	if (veh_detected == true && synched)
	{
		os().debug("Veh detected at %d sec %d ms", time_stamp.sec, time_stamp.ms);
		time* t = &time_stamp;
		os().radio().send(0x8efc, 12, (uint8*) t, Radio::ISENSE_RADIO_TX_OPTION_NONE, this);
		veh_detected = false;
	}

	// Vehicle detection
	uint16 buf_max = 0, buf_min = 4095;
	for (uint8 i = 0; i < buf->count; i++)
	{
		if (buf->buf[2*i] > UPPER_DETECTION_TRIGGER || buf->buf[2*i+1] > UPPER_DETECTION_TRIGGER)
		{
			detecting_veh_max = true;
			uint16 maxval = max(buf->buf[2*i], buf->buf[2*i+1]);
			if (maxval > buf_max)
			{
				buf_max = maxval;
				time_stamp.sec = buf->sec;
				time_stamp.ms = buf->ms;
			}
		}
		else if (buf->buf[2*i] < LOWER_DETECTION_TRIGGER || buf->buf[2*i+1] < LOWER_DETECTION_TRIGGER)
		{
			detecting_veh_min = true;
			uint16 minval = min(buf->buf[2*i], buf->buf[2*i+1]);
			if (minval < buf_min)
			{
				buf_min = minval;
				time_stamp.sec = buf->sec;
				time_stamp.ms = buf->ms;
			}
		}
	}
	if (detecting_veh_max && buf_max < car_max)
	{
		veh_detected  = true;
//		os().debug("max");
		detecting_veh_max = false;
		car_max = 0;
		car_min = 0;
	}
	else if (detecting_veh_min && buf_min > car_min)
	{
		veh_detected  = true;
//		os().debug("min");
		detecting_veh_min = false;
		car_max = 0;
		car_min = 0;
	}
	else if (detecting_veh_max)
		car_max = buf_max;
	else if (detecting_veh_min)
		car_min = buf_min;

	// if auto calibration is enabled
	if (auto_compensate_)
	{

		// calculate average value of all samples in the buffer for both channels
		uint32 amr_avg_0 = 0;
		uint32 amr_avg_1 = 0;
		// sum up samples
		for (uint8 i = 0; i < buf->count; i++)
		{
			amr_avg_0 += buf->buf[2*i];
			amr_avg_1 += buf->buf[2*i+1];
		}
		// device by the number of samples to get avg value
		amr_avg_0 /= buf->count;
		amr_avg_1 /= buf->count;

		// if avg value exceeds thresholds, lauch calibration
		if ((amr_avg_0 < LOWER_COMPENSATION_TRIGGER) || (amr_avg_0 > UPPER_COMPENSATION_TRIGGER) ||(amr_avg_1 < LOWER_COMPENSATION_TRIGGER) || (amr_avg_1 > UPPER_COMPENSATION_TRIGGER))
		{
			os_.debug("launching auto-calibration");
			// add a task for immediate execution that will the the calibration
			os_.add_task( this, (void*)TASK_CALIBRATE);
		}
	}

}
//----------------------------------------------------------------------------
// This method is called if the node receives data of type 03 via the UART
void
	AmrModuleDemoApplication::
	handle_uart_packet( uint8 type, uint8* buf, uint8 length )
{
	if ((type == 5)&&(length>=1))
	{
		if (buf[0] == 0)
		{
			if (length == 1)
			{
				os().debug("Gain set low");
				amr_->set_gain(false);
				os_.add_task( this, (void*)TASK_CALIBRATE);
			} else os_.fatal("Wrong command length.");
		}
		if (buf[0] == 1)
		{
			if (length == 1)
			{
				os().debug("Gain set high");
				amr_->set_gain(true);
				os_.add_task( this, (void*)TASK_CALIBRATE);
			} else os_.fatal("Wrong command length.");
		}
		if (buf[0] == 2)
		{
			if (length == 1)
			{
				// add a task for calibrating the AMR sensor (takes quite long)
				os_.add_task( this, (void*)TASK_CALIBRATE);
			} else os_.fatal("Wrong command length.");
		}
		if (buf[0] == 3)
		{
			if (length == 4)
			{

				if (buf[3] > 25)
				{
					os_.fatal("Warning: buffer size to large (%d), truncated to 25.");
					buf[3]=25;
				}
				if (amr_->set_timing_parameters(buf[1], buf[2], buf[3]))
				{
					sample_count_ = buf[1];
					sample_interval_ = buf[2];
					buffer_size_ = buf[3];
					os_.debug("params set: count=%d, interval=%d, size=%d", buf[1], buf[2], buf[3]);
				}
			} else os_.fatal("Wrong command length.");
		}
		if (buf[0] == 6)
		{
			if (length == 1)
			{
				os_.debug("Starting sensor");
				amr_->start();
			} else os_.fatal("Wrong command length.");
		}

		if (buf[0] == 7)
		{
			if (length == 1)
			{
				os_.debug("Stopping sensor");
				amr_->stop();
			} else os_.fatal("Wrong command length.");
		}
		if (buf[0] == 8)
		{
			if (length == 2)
			{
				if (buf[1] == 0)
					auto_compensate_ = false;
				else
					auto_compensate_ = true;
				os_.debug("Setting auto calibration to %d...", buf[1]);
			} else os_.fatal("Wrong command length.");
		}
		if (buf[0] == 9)
		{
			if (length == 1)
			{
				os_.debug("Operating point0 = %d Operating point1 = %d", amr_->operating_point(0), amr_->operating_point(1));
			} else os_.fatal("Wrong command length.");
		}

	}
	
}

//----------------------------------------------------------------------------
// This method is called if the primitive detection algorithm fires...
void
AmrModuleDemoApplication::
handle_alarm( uint16 alarm_bits[2] )
{
	// this method is called in interrupt context, so
	// add task for immediate execute, to output a message
//	os_.add_task( this, (void*)TASK_ALARM);
}

//----------------------------------------------------------------------------
// This method is called if the primitive detection algorithm fires...
void
AmrModuleDemoApplication::
handle_alarm( Time time )
{
	// this method is called in interrupt context, so
	// add task for immediate execute, to output a message
//	os_.add_task( this, (void*)TASK_ALARM);
}

//----------------------------------------------------------------------------
// This method is called when the calibration is done...
void
AmrModuleDemoApplication::
handle_uint8_data(uint8 data)
{
	os().debug("Calibration done");
	uint8 channel = 0;
	// data contains the result of the calibration
	// 0 = calibration successful on both channels
	// 1 = calibration on channel 0 failed
	// 2 = calibration on channel 1 failed
	// 3 = calibration on both channels failed
	while (data != 0) {
		os_.fatal("Calibration failed for channel %u", channel);
		data >>= 1;
		channel ++;
	}
	// start sensor again
	amr_->start();
}

void
AmrModuleDemoApplication::
confirm (uint8 state, uint8 tries, isense::Time time)
{
	os().debug("Confirmation state=%d tries=%d", state, tries);
}

void
AmrModuleDemoApplication::
receive (uint8 len, const uint8 * buf, ISENSE_RADIO_ADDR_TYPE src_addr, ISENSE_RADIO_ADDR_TYPE dest_addr, uint16 signal_strength, uint16 signal_quality, uint8 seq_no, uint8 interface, Time rx_time)
{
	if (buf[1] == 0)
	{
		time_stamp.synched = true;
		synched = false;
//		os_.add_task( this, (void*)TASK_SYNC);
	}
}
//----------------------------------------------------------------------------
// This method is called when the OS boots, to create an application instance
Application* 
application_factory(Os &os) 
{
		return new AmrModuleDemoApplication(os);
}

#endif
