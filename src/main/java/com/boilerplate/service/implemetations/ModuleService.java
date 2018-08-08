package com.boilerplate.service.implemetations;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IModule;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.ModuleEntity;
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
	public ModuleEntity createModule(ModuleEntity module) throws BadRequestException, ValidationFailedException {
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
		module = mySQLModule.saveModule(module);
		return module;
	}

}
