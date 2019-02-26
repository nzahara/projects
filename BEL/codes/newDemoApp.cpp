/************************************************************************
 ** This file is part of the the iSense project.
 ** Copyright (C) 2006 coalesenses GmbH (http://www.coalesenses.com)
 ** ALL RIGHTS RESERVED.
 ************************************************************************/
#include <isense/application.h>
#include <isense/os.h>
#include <isense/dispatcher.h>
#include <isense/radio.h>
#include <isense/task.h>
#include <isense/timeout_handler.h>
#include <isense/isense.h>
#include <isense/uart.h>
#include <isense/dispatcher.h>
#include <isense/time.h>
#include <isense/button_handler.h>
#include <isense/sleep_handler.h>
#include <isense/modules/pacemate_module/pacemate_module.h>
#include <isense/util/util.h>
#include <isense/modules/core_module/core_module.h>

struct time
{
	bool synched;
	uint32 sec;
	uint16 ms;
};

struct UartTimeBuffer
{
	uint32 device_id;
	uint16 sec;
	uint16 ms;
};

#define MILLISECONDS 1000
//----------------------------------------------------------------------------
/**
 */
// constants for identifying different tasks
#define TASK_SYNC 1
#define TASK_UART_SEND 2
 
using namespace isense;

class iSenseDemoApplication : 
	public isense::Application, 
	public isense::Receiver, 
	public isense::Sender, 
	public isense::Task,
	public isense::TimeoutHandler,
	public isense::SleepHandler,
	public ButtonHandler
{
public:
	iSenseDemoApplication(isense::Os& os);
	
	virtual ~iSenseDemoApplication() ;
	
	///From isense::Application
	virtual void boot (void) ;

	///From isense::SleepHandler
	virtual bool stand_by (void) ; 	// Memory held

	///From isense::SleepHandler
	virtual bool hibernate (void) ;  // Memory not held

	///From isense::SleepHandler
	virtual void wake_up (bool memory_held) ;

	///From isense::ButtonHandler
	virtual void button_down( uint8 button );	
	
	///From isense::Receiver
	virtual void receive (uint8 len, const uint8 * buf, ISENSE_RADIO_ADDR_TYPE src_addr, ISENSE_RADIO_ADDR_TYPE dest_addr, uint16 signal_strength, uint16 signal_quality, uint8 seq_no, uint8 interface, Time rx_time) ;
	
	///From isense::Sender 
	virtual void confirm (uint8 state, uint8 tries, isense::Time time) ;

	///From isense::Task
	virtual void execute( void* userdata ) ;

	///From isense::TimeoutHandler
	virtual void timeout( void* userdata ) ;
private:
	CoreModule* cm_;
	bool buf_num;
	bool synched, time_buf0_flag;
	ISENSE_RADIO_ADDR_TYPE last_addr;
	uint32 tdiff;
	UartTimeBuffer buf0, buf1, time_diff, test_buf, time_buf0, time_buf1, diff;

};

//----------------------------------------------------------------------------
iSenseDemoApplication::
	iSenseDemoApplication(isense::Os& os)
	: isense::Application(os)
	{
	}

//----------------------------------------------------------------------------
iSenseDemoApplication::
	~iSenseDemoApplication()
		
	{
	}

//----------------------------------------------------------------------------
void 
	iSenseDemoApplication::
	boot(void)
	{
        os_.debug("App::boot");
        os_.allow_sleep(false);
        //create CoreModule instance
		cm_ = new CoreModule(os());
		if (cm_ != NULL) // new was successful
		{
			//switch on Core Module LED to show device is awake
			cm_->led_on();

		}
		else
			os().fatal("Could not allocate memory for CoreModule");
		os().dispatcher().add_receiver(this);
        os_.add_timeout_in(Time(MILLISECONDS), this, NULL);
        cm_->led_off();

        tdiff = 0;
        time_buf0_flag = true;
        buf_num = false;
        time_diff.sec = 0;
        time_diff.ms = 0;
        synched = false;
 	}
	
//----------------------------------------------------------------------------
bool 
	iSenseDemoApplication::
	stand_by (void)
	{
		os_.debug("App::sleep");
		return true;
	}
	
//----------------------------------------------------------------------------
bool 
	iSenseDemoApplication::
	hibernate (void)
	{
		os_.debug("App::hibernate");
		return false;
	}
	
//----------------------------------------------------------------------------
void 
	iSenseDemoApplication::
	wake_up (bool memory_held)	
	{
		os_.debug("App::Wakeup");
	}

void
	iSenseDemoApplication::
	button_down( uint8 button )
	{
		
	}
//----------------------------------------------------------------------------
void 
	iSenseDemoApplication::
	execute( void* userdata )
	{
//		os_.debug("exe");
		Uart& uart = os().uart(0);
//		uart.write_buffer((const char *)"Test", 5);
		test_buf.device_id = 1024;
		test_buf.ms = 999;
		test_buf.sec = 1;
//		uint16* buf = &test_buf.sec;
//		uart.write_buffer((const char *)buf, 2);
//		buf = &test_buf.ms;
//		uart.write_buffer((const char *)buf, 2);
		if ((uint32)userdata == TASK_SYNC)
		{
//			os().debug("sync");
			uint8 outbuffer[2];
			outbuffer[0] = 32;
			outbuffer[1] = 0;
			os().radio().send(ISENSE_RADIO_BROADCAST_ADDR, 2,  outbuffer, LinkLayerInterface::ISENSE_RADIO_TX_OPTION_NONE, this);
		}
		if ((uint32)userdata == TASK_UART_SEND)
		{
			os().debug("Uart send");
			os().debug("diff = %d %d", diff.sec, diff.ms);
			uint16* buf = &diff.sec;
			uart.write_buffer((const char *)buf, 2);
			buf = &diff.ms;
			uart.write_buffer((const char *)buf, 2);
//			uart.write_buffer((const char *)buf, 12);
		}
	    //uart.write_buffer((const char *)buf, 12);


	}

//----------------------------------------------------------------------------
void 
	iSenseDemoApplication::
	receive (uint8 len, const uint8 * buf, ISENSE_RADIO_ADDR_TYPE src_addr, ISENSE_RADIO_ADDR_TYPE dest_addr, uint16 signal_strength, uint16 signal_quality, uint8 seq_no, uint8 interface, Time rx_time)
	{
		time* rbuf = (time*) buf;
//		os().debug("Received: %d %d from %x", rbuf->sec, rbuf->ms, src_addr);
//		os().debug("Time diff = %d %d", time_diff.sec, time_diff.ms);
		if (rbuf->synched == false)
		{
			synched  = false;
			buf_num = false;
	    	os().add_task( this, (void*)TASK_SYNC);
		}
		else if (!synched)
		{
			if (last_addr == src_addr)
				buf_num = false;
			if (!buf_num)
			{
				buf0.sec = rbuf->sec;
				buf0.ms = rbuf->ms;
				buf_num = !buf_num;
//				os().debug("rx 0");
			}
			else
			{
				buf1.sec = rbuf->sec;
				buf1.ms = rbuf->ms;
				buf_num = !buf_num;
				uint32 t0 = buf0.sec * 1000 + buf0.ms;
				uint32 t1 = buf1.sec * 1000 + buf1.ms;
				tdiff = max(t1, t0) - min(t1, t0);
				time_diff.sec = tdiff / 1000;
				time_diff.ms = tdiff % 1000;
				synched = true;
//				os().debug("Time diff = %d %d", time_diff.sec, time_diff.ms);
//				os().debug("Test1");
			}
		}
		else
		{
            if (last_addr == src_addr)
            {
				time_buf0_flag = true;
                
            }
			if (time_buf0_flag)
			{
				time_buf0.sec = rbuf->sec;
				time_buf0.ms = rbuf->ms;
				time_buf0_flag = !time_buf0_flag;
			}
			else
			{
				time_buf1.sec = rbuf->sec;
				time_buf1.ms = rbuf->ms;
				uint32 t0 = time_buf0.sec * 1000 + time_buf0.ms;
				uint32 t1 = time_buf1.sec * 1000 + time_buf1.ms;
				uint32 temp = time_diff.sec * 1000 + time_diff.ms;
				uint32 tdiff = max(max(t1, t0) - min(t1, t0), temp) - min(max(t1, t0) - min(t1, t0), temp);
				diff.sec = tdiff / 1000;
				diff.ms = tdiff % 1000;
                if (diff.sec < 3)
                {
    //				os().debug("diff = %d %d", diff.sec, diff.ms);
                    os().add_task(this, (void*) TASK_UART_SEND);
                    time_buf0_flag = !time_buf0_flag;
                }
                else
                {
                    time_buf0.sec = rbuf->sec;
                    time_buf0.ms = rbuf->ms;
                }
			}
//			os().debug("veh detected");
//			buf0.device_id = src_addr;
//			buf0.sec = rbuf->sec;
//			buf0.ms = rbuf->ms;
//			os().add_task(this, (void*) TASK_UART_SEND);

		}
		last_addr = src_addr;
	}
	
//----------------------------------------------------------------------------
void 
	iSenseDemoApplication::
	confirm (uint8 state, uint8 tries, isense::Time time)
	{
//		os().debug("Confirmation state=%d tries=%d", state, tries);
	}

//----------------------------------------------------------------------------
void 
	iSenseDemoApplication::
	timeout( void* userdata )
	{
		os_.add_task( this, NULL);
		os_.add_timeout_in(Time(MILLISECONDS), this, NULL);
	}

//----------------------------------------------------------------------------
/**
  */	
isense::Application* application_factory(isense::Os& os)
{
	return new iSenseDemoApplication(os);
}


/*-----------------------------------------------------------------------
* Source  $Source: $
* Version $Revision: 1.24 $
* Date    $Date: 2006/10/19 12:37:49 $
*-----------------------------------------------------------------------
* $Log$
*-----------------------------------------------------------------------*/
