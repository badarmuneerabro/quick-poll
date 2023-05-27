package com.poll.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.poll.dto.error.ErrorDetails;
import com.poll.dto.error.ValidationError;
import com.poll.exeption.ResourceNotFoundException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler
{
	
	@Autowired
	private MessageSource message;
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) 
	{
		ErrorDetails details = new ErrorDetails();
		details.setTimeStamp(new Date().getTime());
		details.setTitle("Message Not Readable");
		details.setStatus(status.value());
		details.setDeveloperMessage(ex.getClass().getName());
		details.setDetail(ex.getMessage());
		return handleExceptionInternal(ex, details, headers, status, request);
	}
	
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handlerResourceNotFoundException(ResourceNotFoundException rnfe)
	{
		ErrorDetails details = new ErrorDetails();
		
		details.setTimeStamp(new Date().getTime());
		details.setStatus(HttpStatus.NOT_FOUND.value());
		details.setTitle("Resource Not Found");
		details.setDetail(rnfe.getMessage());
		details.setDeveloperMessage(rnfe.getClass().getName());
		
		return new ResponseEntity<>(details, null, HttpStatus.OK);
		
	}
	
	public ResponseEntity<?> handlerMethodArguemntNotValidException(MethodArgumentNotValidException exception)
	{
		ErrorDetails details = new ErrorDetails();
		details.setStatus(HttpStatus.BAD_REQUEST.value());
		details.setTimeStamp(new Date().getTime());
		details.setTitle("Field Validation Error");
		
		details.setDetail("Input validation failed");
		details.setDeveloperMessage(exception.getClass().getName());
		
		List<ObjectError> errors = exception.getBindingResult().getAllErrors();
		
		for(ObjectError e : errors)
		{
			List<ValidationError> list = details.getErrors().get(e.getObjectName());
			
			if(list == null)
			{
				list = new ArrayList<>();
				details.getErrors().put(e.getObjectName(), list);
			}
			
			ValidationError error = new ValidationError();
			error.setCode(e.getCode());
			error.setMessage(message.getMessage(e, null));
			System.out.println("Message Code: " +e);
			
			list.add(error);
			
		}

		
		return new ResponseEntity<>(details, null, HttpStatus.BAD_REQUEST);
	}

}


//List<ObjectError> allErrors = exception.getBindingResult().getAllErrors();
//
//for(ObjectError e : allErrors)
//{
//	List<ValidationError> list = details.getErrors().get(e.getObjectName());
//	
//	if(list == null) {
//		list = new ArrayList<>();
//		details.getErrors().put(e.getObjectName(), list);
//	}
//	
//	ValidationError error = new ValidationError();
//	error.setCode(e.getCode());
//	error.setMessage(e.getDefaultMessage());
//	list.add(error);
//	
//}