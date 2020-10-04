package com.example.gethelp;

import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerProblem {



        private String title;
        private String description;

        public void CustomerProblem(String title, String description) {
            this.title = title;
            this.description = description;
        }

        public void CustomerProblem() {
            //Empty constructor needed
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

}

