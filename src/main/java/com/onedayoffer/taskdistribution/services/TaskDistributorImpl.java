package com.onedayoffer.taskdistribution.services;

import com.onedayoffer.taskdistribution.DTO.EmployeeDTO;
import com.onedayoffer.taskdistribution.DTO.TaskDTO;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class TaskDistributorImpl implements TaskDistributor {

  @Override
  public void distribute(List<EmployeeDTO> employees, List<TaskDTO> tasks) {
    tasks.sort(Comparator.comparingInt(TaskDTO::getPriority));
    for (TaskDTO task : tasks) {
      EmployeeDTO availableEmployee = findAvailableEmployee(employees, task.getLeadTime());
      if (availableEmployee != null) {
        availableEmployee.addTask(task);
      }
    }
  }

  private EmployeeDTO findAvailableEmployee(List<EmployeeDTO> employees, int leadTime) {
    for (EmployeeDTO employee : employees) {
      if (employee.getTotalLeadTime() + leadTime <= 420) {
        return employee;
      }
    }
    return null;
  }
}
