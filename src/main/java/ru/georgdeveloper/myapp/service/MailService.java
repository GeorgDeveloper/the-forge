// Указываем пакет, к которому принадлежит данный сервис
package ru.georgdeveloper.myapp.service;

// Импортируем необходимые классы
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import ru.georgdeveloper.myapp.domain.User;
import tech.jhipster.config.JHipsterProperties;

/**
 * Сервис для асинхронной отправки электронных писем.
 * <p>
 * Использует аннотацию {@link Async} для асинхронной отправки писем.
 */
@Service
public class MailService {

    // Логгер для записи событий
    private static final Logger LOG = LoggerFactory.getLogger(MailService.class);

    // Константа для ключа пользователя в контексте шаблона
    private static final String USER = "user";

    // Константа для ключа базового URL в контексте шаблона
    private static final String BASE_URL = "baseUrl";

    // Настройки приложения из JHipster
    private final JHipsterProperties jHipsterProperties;

    // Отправитель электронной почты
    private final JavaMailSender javaMailSender;

    // Источник сообщений для интернационализации
    private final MessageSource messageSource;

    // Движок шаблонов Thymeleaf
    private final SpringTemplateEngine templateEngine;

    /**
     * Конструктор сервиса с внедрением зависимостей.
     *
     * @param jHipsterProperties настройки JHipster
     * @param javaMailSender отправитель почты
     * @param messageSource источник сообщений
     * @param templateEngine движок шаблонов
     */
    public MailService(
        JHipsterProperties jHipsterProperties,
        JavaMailSender javaMailSender,
        MessageSource messageSource,
        SpringTemplateEngine templateEngine
    ) {
        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    /**
     * Асинхронно отправляет электронное письмо.
     *
     * @param to адрес получателя
     * @param subject тема письма
     * @param content содержимое письма
     * @param isMultipart является ли письмо multipart (с вложениями)
     * @param isHtml является ли содержимое HTML
     */
    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        sendEmailSync(to, subject, content, isMultipart, isHtml);
    }

    /**
     * Синхронная реализация отправки письма.
     *
     * @param to адрес получателя
     * @param subject тема письма
     * @param content содержимое письма
     * @param isMultipart является ли письмо multipart
     * @param isHtml является ли содержимое HTML
     */
    private void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        // Логируем попытку отправки письма
        LOG.debug(
            "Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart,
            isHtml,
            to,
            subject,
            content
        );

        // Создаем MIME сообщение
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            // Настраиваем сообщение с помощью Spring-хелпера
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom()); // Устанавливаем отправителя из настроек
            message.setSubject(subject);
            message.setText(content, isHtml); // Устанавливаем текст письма (HTML или plain text)

            // Отправляем письмо
            javaMailSender.send(mimeMessage);
            LOG.debug("Sent email to User '{}'", to);
        } catch (MailException | MessagingException e) {
            // Логируем ошибку при отправке
            LOG.warn("Email could not be sent to user '{}'", to, e);
        }
    }

    /**
     * Асинхронно отправляет письмо на основе шаблона Thymeleaf.
     *
     * @param user пользователь-получатель
     * @param templateName имя шаблона письма
     * @param titleKey ключ темы письма для интернационализации
     */
    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        sendEmailFromTemplateSync(user, templateName, titleKey);
    }

    /**
     * Синхронная реализация отправки письма по шаблону.
     *
     * @param user пользователь-получатель
     * @param templateName имя шаблона
     * @param titleKey ключ темы письма
     */
    private void sendEmailFromTemplateSync(User user, String templateName, String titleKey) {
        // Проверяем наличие email у пользователя
        if (user.getEmail() == null) {
            LOG.debug("Email doesn't exist for user '{}'", user.getLogin());
            return;
        }

        // Устанавливаем локаль пользователя
        Locale locale = Locale.forLanguageTag(user.getLangKey());

        // Создаем контекст для шаблона
        Context context = new Context(locale);
        context.setVariable(USER, user); // Добавляем пользователя в контекст
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl()); // Добавляем базовый URL

        // Генерируем содержимое письма из шаблона
        String content = templateEngine.process(templateName, context);

        // Получаем тему письма с учетом локали
        String subject = messageSource.getMessage(titleKey, null, locale);

        // Отправляем письмо
        sendEmailSync(user.getEmail(), subject, content, false, true);
    }

    /**
     * Асинхронно отправляет письмо активации аккаунта.
     *
     * @param user пользователь, которому отправляется письмо активации
     */
    @Async
    public void sendActivationEmail(User user) {
        LOG.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplateSync(user, "mail/activationEmail", "email.activation.title");
    }

    /**
     * Асинхронно отправляет письмо о создании аккаунта.
     *
     * @param user пользователь, которому отправляется письмо
     */
    @Async
    public void sendCreationEmail(User user) {
        LOG.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplateSync(user, "mail/creationEmail", "email.activation.title");
    }

    /**
     * Асинхронно отправляет письмо для сброса пароля.
     *
     * @param user пользователь, запросивший сброс пароля
     */
    @Async
    public void sendPasswordResetMail(User user) {
        LOG.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplateSync(user, "mail/passwordResetEmail", "email.reset.title");
    }
}
