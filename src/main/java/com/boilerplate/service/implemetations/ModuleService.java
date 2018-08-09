package com.boilerplate.service.implemetations;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IModule;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.ModuleEntity;
import com.boilerplate.java.entities.ModuleQuizEntity;
import com.boilerplate.java.entities.SubModuleEntity;
import com.boilerplate.service.interfaces.IModuleService;

/**
 * Thjis class implements IModuleService
 * 
 * @author ruchi
 *
 */
public class ModuleService implements IModuleService {

	/**
	 * This is the autowired instance of IModule
	 */
	@Autowired
	IModule mySQLModule;

	/**
	 * This is the setter for IModule instance
	 * 
	 * @param mySQLModule
	 */
	public void setMySQLModule(IModule mySQLModule) {
		this.mySQLModule = mySQLModule;
	}

	/**
	 * @see IModuleService.createModule
	 */
	@Override
	public ModuleEntity createModule(ModuleEntity module) throws Exception {
		// Check if module is not null
		if (module == null)
			throw new BadRequestException("ModuleEntity", "Given module entity is null", null);
		// Check if module id is null or not
		if (module.getId() != null)
			throw new BadRequestException("ModuleEntity", "Module entity should not contain Id", null);
		// Validate module entity
		module.validate();
		// save module in MySQL database
		module = mySQLModule.saveModule(module);

		return module;
	}

	/**
	 * @see IModuleService.updateModule
	 */
	@Override
	public ModuleEntity updateModule(ModuleEntity module)
			throws ValidationFailedException, BadRequestException, NotFoundException {
		// Check if module is not null
		if (module == null)
			throw new BadRequestException("ModuleEntity", "Given module entity is null", null);
		// Check if module id is null or not
		if (module.getId() == null)
			throw new BadRequestException("ModuleEntity", "Module Id is null", null);
		// check if module exists in system
		if (mySQLModule.getModule(module.getId()) == null)
			throw new NotFoundException("ModuleEntity", "Module not found", null);
		// Validate module entity
		module.validate();
		// save module in MySQL database
		module = mySQLModule.updateModule(module);
		return module;
	}

	/**
	 * @see IModuleService.createSubModule
	 */
	@Override
	public ModuleQuizEntity createModuleQuiz(ModuleQuizEntity moduleQuiz) throws Exception {
		// Check if no module quiz entity is provided
		if (moduleQuiz == null)
			throw new BadRequestException("ModuleQuizEntity", "Given module quiz entity is null", null);
		// module id should not be input here
		if (moduleQuiz.getId() != null)
			throw new BadRequestException("ModuleQuizEntity",
					"Module quiz entity should not contain Id while creation.", null);
		// Validate module quiz entity
		moduleQuiz.validate();
		// save module quiz entity in MySQL database
		return mySQLModule.saveModuleQuiz(moduleQuiz);
	}

	/**
	 * @see IModule.createSubModule
	 */

	@Override
	public SubModuleEntity createSubModule(SubModuleEntity subModule) throws Exception {
		// Check if sub module is not null
		if (subModule == null)
			throw new BadRequestException("SubModuleEntity", "Given SubModuleEntity is null", null);
		// Check if subModule id is null or not
		if (subModule.getId() != null)
			throw new BadRequestException("SubModuleEntity", "SubModuleEntity should not contain Id", null);
		// validate sub module entity
		subModule.validate();
		// check if module id of this sub module entity exists or not
		if (mySQLModule.getModule(subModule.getModuleId()) == null)
			throw new NotFoundException("ModuleEntity", "Module not found", null);
		// save sub module entity in database
		return mySQLModule.saveSubModule(subModule);
	}

	/**
	 * @see IModuleService.updateSubModule
	 */
	@Override
	public SubModuleEntity updateSubModule(SubModuleEntity subModule)
			throws ValidationFailedException, BadRequestException, NotFoundException {
		// Check if sub module is not null
		if (subModule == null)
			throw new BadRequestException("SubModuleEntity", "Given module entity is null", null);
		// Check if subModule id is null or not
		if (subModule.getId() != null)
			throw new BadRequestException("SubModuleEntity", "SubModuleEntity should not contain Id", null);
		// check if sub module exists in system
		if (mySQLModule.getSubModule(subModule.getId()) == null)
			throw new NotFoundException("SubModuleEntity", "subModule not found", null);
		// Validate subModule entity
		subModule.validate();
		// save subModule in MySQL database
		return mySQLModule.updateSubModule(subModule);
	}

}
