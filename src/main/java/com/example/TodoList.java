package com.example;


import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@SpringComponent
@UIScope
public class TodoList extends VerticalLayout implements TodoChangeListener{

    @Autowired
    TodoRepository repository;
    private List<Todo> todos;

    @PostConstruct
    void init() {
        setSpacing(true);

        update();
    }

    private void update() {
        setTodos(repository.findAll());
    }

    public void setTodos(List<Todo> todos) {
        this.todos = todos;
        removeAllComponents();

        todos.forEach(todo -> {
            addComponent(new TodoLayout(todo, this));
        });
    }

    public void save(Todo todo) {
        repository.save(todo);
        update();
    }

    @Override
    public void todoChanged(Todo todo) {
        save(todo);
    }

    public void deleteCompleted() {
        repository.deleteInBatch(
                todos.stream().filter(Todo::isDone).collect(Collectors.toList()));

        update();
    }
}
