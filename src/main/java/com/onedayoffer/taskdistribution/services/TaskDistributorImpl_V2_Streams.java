package com.onedayoffer.taskdistribution.services;

import com.onedayoffer.taskdistribution.DTO.EmployeeDTO;
import com.onedayoffer.taskdistribution.DTO.TaskDTO;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

public class TaskDistributorImpl_V2_Streams implements TaskDistributor {
  @Override
  public void distribute(List<EmployeeDTO> employees, List<TaskDTO> tasks) {
    tasks.sort(Comparator.comparingInt(TaskDTO::getPriority));
    tasks.forEach(
        task ->
            employees.stream()
                .filter(employee -> employee.getTotalLeadTime() + task.getLeadTime() <= 420)
                .findFirst()
                .ifPresent(availableEmployee -> availableEmployee.addTask(task)));
  }
}
