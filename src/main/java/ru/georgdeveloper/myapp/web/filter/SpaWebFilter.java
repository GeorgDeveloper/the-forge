package ru.georgdeveloper.myapp.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Фильтр для одностраничных приложений (SPA).
 * Перенаправляет все необработанные запросы (кроме тех, что содержат точку)
 * на клиентский index.html для обработки маршрутизации на стороне клиента.
 */
public class SpaWebFilter extends OncePerRequestFilter {

    /**
     * Основной метод фильтрации, вызываемый для каждого запроса.
     * Перенаправляет запросы, которые должны обрабатываться клиентским приложением.
     *
     * @param request HTTP-запрос
     * @param response HTTP-ответ
     * @param filterChain цепочка фильтров
     * @throws ServletException если возникает ошибка сервлета
     * @throws IOException если возникает ошибка ввода/вывода
     */
    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Получаем URI запроса, удаляя контекстный путь (если есть)
        String path = request.getRequestURI().substring(request.getContextPath().length());

        // Проверяем, нужно ли перенаправлять запрос на index.html
        if (
            !path.startsWith("/api") && // Не API-запросы
            !path.startsWith("/management") && // Не запросы к management
            !path.startsWith("/v3/api-docs") && // Не запросы к документации API
            !path.contains(".") && // Не содержит точки (не статический файл)
            path.matches("/(.*)") // Соответствует шаблону пути
        ) {
            // Перенаправляем запрос на index.html для обработки клиентским роутером
            request.getRequestDispatcher("/index.html").forward(request, response);
            return;
        }

        // Если запрос не подходит под условия выше, передаем его дальше по цепочке фильтров
        filterChain.doFilter(request, response);
    }
}
