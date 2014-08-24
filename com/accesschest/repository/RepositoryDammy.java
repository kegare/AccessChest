package com.accesschest.repository;

public class RepositoryDammy extends Repository
{
	public RepositoryDammy(int grade)
	{
		super(new DataManagerArray(), 0, grade, false);
		this.grade = grade;
	}

	@Override
	public String getInventoryName()
	{
		return "dammy";
	}
}