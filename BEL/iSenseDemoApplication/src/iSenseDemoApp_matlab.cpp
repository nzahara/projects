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


#define MILLISECONDS 1000
//----------------------------------------------------------------------------
/**
 */
 
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
		//os_.debug("exe");
		//os().debug("eat chicken");
		//Uart& uart = os().uart(0);
		uint8 outbuffer[4];
		outbuffer[0] = 32;
		outbuffer[1] = 1;
		outbuffer[2] = 2;
		outbuffer[3] = 3;
	    //uart.write_buffer((const char *)buf, 12);
	    //os().radio().send(ISENSE_RADIO_BROADCAST_ADDR, 4,  outbuffer, LinkLayerInterface::ISENSE_RADIO_TX_OPTION_ACK, this);

	}

//----------------------------------------------------------------------------
void 
	iSenseDemoApplication::
	receive (uint8 len, const uint8 * buf, ISENSE_RADIO_ADDR_TYPE src_addr, ISENSE_RADIO_ADDR_TYPE dest_addr, uint16 signal_strength, uint16 signal_quality, uint8 seq_no, uint8 interface, Time rx_time)
	{
		Uart& uart = os().uart(0);
		BufferData* rbuf = (BufferData*) buf;
		//os().debug("Received: %d %d from %x",  rbuf->buf[0], rbuf->sec, src_addr);
		for (uint8 i = 0; i < rbuf->count; i++)
		{
			char s[5] = "0000";
			snprintf(s, 4, "%d", rbuf->buf[2*i]);
			uart.write_buffer(s, 5);
			snprintf(s, 4, "%d", rbuf->buf[2*i+1]);
			uart.write_buffer(s, 5);
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
