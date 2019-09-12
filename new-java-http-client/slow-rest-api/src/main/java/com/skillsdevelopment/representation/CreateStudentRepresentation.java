package com.skillsdevelopment.representation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.skillsdevelopment.server.model.Student;

import java.util.UUID;

public class CreateStudentRepresentation {
        private final String name;
        
        public CreateStudentRepresentation(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
}
