package com.events.event_management.Service;

import com.events.event_management.Entity.Event;
import com.events.event_management.Entity.UserApp;
import com.events.event_management.Repository.EventRepo;
import com.events.event_management.Repository.UserAppRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EventRepo eventRepo;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @Scheduled(fixedRate = 3600000) // Exécuté toutes les heures
    public void sendEventReminders() {
        List<Event> upcomingEvents = eventRepo.findAll();
        Date now = new Date();

        for (Event event : upcomingEvents) {
            // Vérifier si l'événement est dans les prochaines 24 heures
            long timeDifference = event.getDate().getTime() - now.getTime();
            if (timeDifference > 0 && timeDifference <= 2 * 60 * 1000) {
                sendReminderEmail(event);
            }
        }
    }

    public void sendReminderEmail(Event event) {
        for (UserApp participant : event.getParticipants()) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(participant.getEmail());
            message.setSubject("Rappel : Événement à venir !");
            message.setText("Bonjour " + participant.getUsername() + ",\n\n" +
                    "Cet e-mail est un rappel pour l'événement : " + event.getEventName() +
                    " prévu le " + event.getDate() + ".\n\n" +
                    "Description : " + event.getDescription() + "\n\n" +
                    "Cordialement,\nL'équipe Event Management.");
            mailSender.send(message);
        }
    }

}
