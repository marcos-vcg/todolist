package br.com.marcoscosta.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.marcoscosta.todolist.user.IUserRepository;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    IUserRepository userRepository;

    // Todas as requisições passam pelo filtro antes de chegar ao controller
    // respectivo

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var servletPath = request.getServletPath();
        if (servletPath.startsWith("/tasks/")) {

            // Pegar a autenticação(usuário e senha)
            var authorization = request.getHeader("Authorization"); // Basic Hksdfijsidf
            var authEncoded = authorization.substring("Basic".length()).trim(); // Hksdfijsidf
            byte[] authDecode = Base64.getDecoder().decode(authEncoded); // @sakdjf
            var authString = new String(authDecode); // marcos:12345
            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];

            // Validar (usuário)
            var user = userRepository.findByUsername(username);
            if (user == null) {
                response.sendError(401, "Usuário sem autorização!");
            } else {
                // Validar (senha)
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (passwordVerify.verified) {
                    // Resgata o usuário da requisição
                    request.setAttribute("idUser", user.getId());
                    // Segue o caminho
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }
            }
        } else {
            filterChain.doFilter(request, response);
        }

    }
}

// @Component
// public class FilterTaskAuth implements Filter {

// // Todas as requisições passam pelo filtro antes de chegar ao controller
// respectivo

// @Override
// public void doFilter(ServletRequest request, ServletResponse response,
// FilterChain chain)
// throws IOException, ServletException {
// chain.doFilter(request, response);
// }

// }
