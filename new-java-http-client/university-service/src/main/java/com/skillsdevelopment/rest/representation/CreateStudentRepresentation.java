package com.skillsdevelopment.rest.representation;

public class CreateStudentRepresentation {
        private final String name;
        
        public CreateStudentRepresentation(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
}
