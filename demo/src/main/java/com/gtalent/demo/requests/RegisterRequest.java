package com.gtalent.demo.requests;

public class RegisterRequest {
        private String username;
        private String email;
        private String pwd;

        public RegisterRequest(String username, String email, String pwd) {
            this.username = username;
            this.email = email;
            this.pwd = pwd;
        }

        public RegisterRequest() {
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }
    }
