package com.poll.dto;

import com.poll.model.Option;

public class VoteResult 
{
	private Option option;
	private int count;
	
	
	public VoteResult(Option option) 
	{
		this.option = option;
	}
	
	public Option getOption() {
		return option;
	}
	public void setOption(Option option) {
		this.option = option;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public void updateCount()
	{
		++count;
	}

}
