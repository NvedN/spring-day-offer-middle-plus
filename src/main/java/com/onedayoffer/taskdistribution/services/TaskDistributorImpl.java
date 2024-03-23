package com.onedayoffer.taskdistribution.services;

import com.onedayoffer.taskdistribution.DTO.EmployeeDTO;
import com.onedayoffer.taskdistribution.DTO.TaskDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
public class TaskDistributorImpl implements TaskDistributor {
  private static final int WORKING_HOURS_IN_MINUTES = 420;

  @Override
  public void distribute(List<EmployeeDTO> employees, List<TaskDTO> tasks) {
    log.info("Starting task distribution among employees.");

    tasks.sort(Comparator.comparingInt(TaskDTO::getPriority));
    logTaskDistribution(employees, "before distribution");

    Iterator<TaskDTO> taskIterator = tasks.iterator();
    while (taskIterator.hasNext()) {
      TaskDTO task = taskIterator.next();
      EmployeeDTO assignedEmployee = findAvailableEmployee(employees, task.getLeadTime());

      if (assignedEmployee != null) {
        assignedEmployee.addTask(task);
        taskIterator.remove();
        log.info(
            "Task with priority {} assigned to employee {}",
            task.getPriority(),
            assignedEmployee.getFio());
      }
    }

    if (!tasks.isEmpty()) {
      logUnassignedTasks(tasks);
    }

    logTaskDistribution(employees, "after distribution");
    log.info("Task distribution completed.");
  }

  private EmployeeDTO findAvailableEmployee(List<EmployeeDTO> employees, int leadTime) {
    return employees.stream()
        .filter(e -> e.getTotalLeadTime() + leadTime <= WORKING_HOURS_IN_MINUTES)
        .findFirst()
        .orElse(null);
  }

  private void logTaskDistribution(List<EmployeeDTO> employees, String state) {
    employees.forEach(
        employee ->
            log.info(
                "State of employee {} {} task distribution - tasks: {}, total lead time: {}",
                employee.getFio(),
                state,
                employee.getTasks(),
                employee.getTotalLeadTime()));
  }

  private void logUnassignedTasks(List<TaskDTO> tasks) {
    tasks.forEach(
        task ->
            log.warn(
                "Task \"{}\" with priority {} could not be assigned to any employee.",
                task.getName(),
                task.getPriority()));
  }
}
