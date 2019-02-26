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

#define MILLISECONDS 1000
//----------------------------------------------------------------------------
/**
 */
// constants for identifying different tasks
#define TASK_SYNC 1
 
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
	time buf0, buf1, time_diff;
	bool synched;
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
//		os().debug("eat chicken");
		char buf[12] = "Test";
		Uart& uart = os().uart(0);
//		if ((uint32)userdata == TASK_SYNC)
//		{
//			os().debug("sync");
//			uint8 outbuffer[2];
//			outbuffer[0] = 32;
//			outbuffer[1] = 0;
//			os().radio().send(ISENSE_RADIO_BROADCAST_ADDR, 2,  outbuffer, LinkLayerInterface::ISENSE_RADIO_TX_OPTION_NONE, this);
//		}
	    uart.write_buffer((const char *)buf, 12);

	}

//----------------------------------------------------------------------------
void 
	iSenseDemoApplication::
	receive (uint8 len, const uint8 * buf, ISENSE_RADIO_ADDR_TYPE src_addr, ISENSE_RADIO_ADDR_TYPE dest_addr, uint16 signal_strength, uint16 signal_quality, uint8 seq_no, uint8 interface, Time rx_time)
	{
//		Uart& uart = os().uart(0);
		time* rbuf = (time*) buf;
		os().debug("Received: %d %d %d from %x",  rbuf->synched==true, rbuf->sec, rbuf->ms, src_addr);
		os().debug("Synched = %d Time diff = %d %d", synched, time_diff.sec, time_diff.ms);
		if (rbuf->synched == false)
		{
			synched  = false;
	    	os().add_task( this, (void*)TASK_SYNC);
		}
		else if (!synched)
		{
			if (!buf_num)
			{
				buf0.sec = rbuf->sec;
				buf0.ms = rbuf->ms;
				buf_num = !buf_num;
			}
			else
			{
				buf1.sec = rbuf->sec;
				buf1.ms = rbuf->ms;
				buf_num = !buf_num;
				time_diff.sec = buf1.sec - buf0.sec;
				time_diff.ms = buf1.ms - buf0.ms;
				synched = true;
			}
		}
		else
		{
			if (!buf_num)
			{
				buf0.sec = rbuf->sec;
				buf0.ms = rbuf->ms;
				buf_num = !buf_num;
			}
			else
			{
				buf1.sec = rbuf->sec;
				buf1.ms = rbuf->ms;
				buf_num = !buf_num;
			}
		}
	}
	
//----------------------------------------------------------------------------
void 
	iSenseDemoApplication::
	confirm (uint8 state, uint8 tries, isense::Time time)
	{
		os().debug("Confirmation state=%d tries=%d", state, tries);
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
