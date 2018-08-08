package com.boilerplate.service.interfaces;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.ModuleEntity;

public interface IModuleService {

	public ModuleEntity createModule(ModuleEntity module) throws BadRequestException, ValidationFailedException;

}
