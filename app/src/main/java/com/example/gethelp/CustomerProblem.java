package com.example.gethelp;

public class CustomerProblem {
        private String id;
        private String title;
        private String description;

        public void CustomerProblem(String title, String description) {
            this.title = title;
            this.description = description;
        }

        public void CustomerProblem() {
            //Empty constructor needed
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }
}

