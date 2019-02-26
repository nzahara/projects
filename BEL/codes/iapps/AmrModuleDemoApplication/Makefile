all: JN5148 JN5139R JN5139R1
	

JN5139R:
	@echo "----- Building for JENNIC 5139R target ------"
	@make JENNIC_CHIP=JN5139R -s -f Makefile.jennic
	@echo "----- Done  ------"

JN5139R1:
	@echo "----- Building for JENNIC 5139R1 target ------"
	@make JENNIC_CHIP=JN5139R1 -f Makefile.jennic
	@echo "----- Done  ------"

JN5148:
	@echo "----- Building for JENNIC 5148 target ------"
	@make JENNIC_CHIP=JN5148 -j4 -s -f Makefile.jennic.5148
	@echo "----- Done  ------"


clean: clean-JN5139R clean-JN5139R1 clean-JN5148
	
clean-JN5139R:
	@make JENNIC_CHIP=JN5139R -s -f Makefile.jennic clean
	@echo "----- JN5139R cleaned ------"

clean-JN5139R1:
	@make JENNIC_CHIP=JN5139R1 -s -f Makefile.jennic clean
	@echo "----- JN5139R1 cleaned ------"

clean-JN5148:
	@make JENNIC_CHIP=JN5148 -s -f Makefile.jennic.5148 clean
	@echo "----- JN5148 cleaned ------"

	
.PHONY: all buildbin debug JN5139R JN5139R1 JN5148 clean-JN5139R clean-JN5139R1 clean-JN5148