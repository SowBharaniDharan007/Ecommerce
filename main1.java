import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class eCommerceServlet extends HttpServlet {
    private List<Product> productList = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        // Sample product data
        productList.add(new Product(1, "Product 1", 10.00));
        productList.add(new Product(2, "Product 2", 20.00));
        productList.add(new Product(3, "Product 3", 30.00));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("login".equals(action)) {
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else if ("cart".equals(action)) {
            List<Product> cart = (List<Product>) request.getSession().getAttribute("cart");
            if (cart == null) {
                cart = new ArrayList<>();
                request.getSession().setAttribute("cart", cart);
            }
            request.setAttribute("cart", cart);
            request.getRequestDispatcher("/cart.jsp").forward(request, response);
        } else {
            request.setAttribute("products", productList);
            request.getRequestDispatcher("/products.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("addToCart".equals(action)) {
            int productId = Integer.parseInt(request.getParameter("productId"));
            Product product = getProductById(productId);
            List<Product> cart = (List<Product>) request.getSession().getAttribute("cart");
            if (cart == null) {
                cart = new ArrayList<>();
                request.getSession().setAttribute("cart", cart);
            }
            cart.add(product);
            response.sendRedirect("eCommerceServlet?action=cart");
        } else if ("loginSubmit".equals(action)) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            if ("admin".equals(username) && "password".equals(password)) {
                request.getSession().setAttribute("user", username);
                response.sendRedirect("eCommerceServlet");
            } else {
                response.sendRedirect("eCommerceServlet?action=login&error=true");
            }
        }
    }

    private Product getProductById(int id) {
        for (Product product : productList) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }
}
