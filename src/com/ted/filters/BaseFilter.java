package com.ted.filters;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ted.controller.Addresses;

public abstract class BaseFilter implements Addresses, Filter {

	Logger log = LoggerFactory.getLogger(this.getClass());
}
