/****************************************************
 *													*
 *  iSense Core Module Demo Application				*
 *  Version: 	1.1									*
 *  Date: 		11.01.2010							*
 *  Targets:	JN5139R1, JN5139R, JN5148           *
 * 	Author: 	Carsten Buschmann					*
 * 													*
 * 	Copyright 2006-2010								*
 * 	ALL RIGHTS RESERVED								*
 * 	coalesenses GmbH 								*
 * 	http://www.coalesenses.com						*
 * 													*
 ****************************************************/

#include <isense/config.h>
#include <isense/gpio.h>
#include <isense/os.h>
#include <isense/types.h>
#include <isense/application.h>
#include <isense/task.h>
#include <isense/time.h>
#include <isense/sleep_handler.h>
#include <isense/modules/core_module/core_module.h>

using namespace isense;

#define TASK_WAKE 1  //stay awake
#define TASK_SLEEP 2 // go to sleep


//--------------------------------------------------------------
class CoreModuleDemoApplication :
	public Application,
	public Task,
	public SleepHandler
{

public:
	CoreModuleDemoApplication(Os &os);
	// inherited from application, called upon device boot
	void boot();
	// inherited from SleepHandler, called upon wake->sleep transition
 	bool stand_by (void);
	// inherited from SleepHandler, called upon sleep->wake transition
 	void wake_up (bool memory_held);
 	// inherited from Task, called when registered task is due
	void execute( void* userdata );
private:
	//Pointer to CoreModule instance
	CoreModule* cm_;
};

//--------------------------------------------------------------
CoreModuleDemoApplication::
CoreModuleDemoApplication(Os &_os) :
	Application(_os),
	cm_(NULL)
{
}

//--------------------------------------------------------------
void
CoreModuleDemoApplication::
boot ()
{
	#if (ISENSE_RADIO_ADDRESS_LENGTH == 16)
		os().debug("Booting Core Module Demo Application, id=%x", os().id());
	#else
		os().debug("Booting Core Module Demo Application, id=%lx", os().id());
	#endif

	//create CoreModule instance
	cm_ = new CoreModule(os());
	if (cm_ != NULL) // new was successful
	{
		//switch on Core Module LED to show device is awake
		cm_->led_on();
		//set regulator to continuous mode
		cm_->set_regulator_mode(CoreModule::continuous_mode);
		// register application as sleep handler
		// --> wake_up and stand_by call upon sleep state changes
		os().add_sleep_handler(this);
		// device should stay awake for the first five seconds
		// forbid sleep now
		os().allow_sleep(false);
		// register task in 5 seconds, to allow sleeping then
		os().add_task_in(Time(5000), this, (void*)TASK_SLEEP);
	} else
		os().fatal("Could not allocate memory for CoreModule");
}

//--------------------------------------------------------------
void
CoreModuleDemoApplication::
wake_up (bool memory_held)
{
	//switch on Core Module LED to show device is awake
	cm_->led_on();
	os().debug("Waking up...");
	//set regulator to continuous mode
	cm_->set_regulator_mode(CoreModule::continuous_mode);
}

//--------------------------------------------------------------
bool
CoreModuleDemoApplication::
stand_by () // Memory held
{
	//switch off Core Module LED to show device is sleeping
	cm_->led_off();
	os().debug("Standing by...");
	//set regulator to power save mode
	cm_->set_regulator_mode(CoreModule::power_save_mode);
	return true;
}

//--------------------------------------------------------------
void
CoreModuleDemoApplication::
execute(void *userdata )
{
	if ((uint32)userdata == TASK_WAKE)
	{
		os().debug("Staying awake");
		//forbid sleeping
		os().allow_sleep(false);
		// register Task to allow sleeping again in 5 seconds
		os().add_task_in(Time(5000), this, (void*)TASK_SLEEP);
		//measure and output Core Module supply voltage
		uint16 voltage = cm_->supply_voltage();
		os().debug("Core Module supply voltage is %d", voltage);
	}
	else if ((uint32)userdata == TASK_SLEEP)
	{
		//allow sleeping
		os().allow_sleep(true);
		os().debug("Allowing Sleep");
		// register task in order to wake up and then forbid sleeping again
		os().add_task_in(Time(5000), this, (void*)TASK_WAKE);
	}
}

//--------------------------------------------------------------
Application* application_factory(Os &os)
{
	//create application instance
	return new CoreModuleDemoApplication(os);
}


