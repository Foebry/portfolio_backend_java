package services;

import java.util.Calendar;
import java.util.Stack;

import controllers.ExperienceController;
import entities.Company;
import entities.Experience;

public class Factory {

    ExperienceController homeController;

    public Factory(Database db) {
        Stack<Experience> experiences = createExperiences(5);

        this.homeController = new ExperienceController(experiences, db);
    }

    public ExperienceController getHomeController() {
        return this.homeController;
    }

    public static Stack<Experience> createExperiences(int amount) {
        final Stack<Experience> experiences = new Stack<Experience>();

        for (int i = 0; i < amount; i++) {
            Company company = new Company("Yucopia", "Address");
            Calendar startDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            Experience experience = new Experience(startDate, endDate, company, "Fullstack Developer", "Stuff");

            experiences.push(experience);
        }

        return experiences;
    }
}
