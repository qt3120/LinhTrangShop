package com.user.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.entity.CartItem;
import com.entity.Color;
import com.entity.Size;
import com.repository.ColorRepository;
import com.repository.SizeRepository;
import com.service.ProductService;

@Controller
@RequestMapping(value = "/cart")
public class CartController {

    @Autowired
    private ProductService productService;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private ColorRepository colorRepository;

    @RequestMapping()
    public String index() {
        return "user/cart/index";
    }

    @GetMapping("/buy/{id}/{colorId}/{sizeId}/{quantity}")
    public String buy(@PathVariable("id") int id, HttpSession session, @PathVariable("colorId") int colorId,
                      @PathVariable("quantity") int quantity, @PathVariable("sizeId") int sizeId) {
        Size size = sizeRepository.findById(sizeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Size Id:" + id));
        Color color = colorRepository.findById(colorId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Color Id:" + id));
        if (session.getAttribute("cart") == null) {
            List<CartItem> cart = new ArrayList<CartItem>();
            cart.add(new CartItem(productService.findById(id), quantity, color, size));
            session.setAttribute("cart", cart);
            session.setAttribute("totalAmount", this.totalAmount(cart));
        } else {
            List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
            int index = this.exists(id, cart, colorId, sizeId);
            if (index == -1) {
                cart.add(new CartItem(productService.findById(id), quantity, color, size));
            } else {
                int quantity2 = cart.get(index).getQuantity() + quantity;
                cart.get(index).setQuantity(quantity2);
            }
            session.setAttribute("cart", cart);
            session.setAttribute("totalAmount", this.totalAmount(cart));
        }

        return "redirect:/cart";
    }

    @GetMapping("/remove/{id}/{sizeId}/{colorId}")
    public String remove(@PathVariable("id") int id, HttpSession session, @PathVariable("colorId") int colorId,
                         @PathVariable("sizeId") int sizeId) {

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        int index = this.exists(id, cart, colorId, sizeId);
        cart.remove(index);
        session.setAttribute("cart", cart);
        session.setAttribute("totalAmount", this.totalAmount(cart));
        return "redirect:/cart";
    }

    private int exists(int id, List<CartItem> cart, int colorId, int sizeId) {
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getProduct().getId() == id && cart.get(i).getColorId().getId() == colorId
                    && cart.get(i).getSizeId().getId() == sizeId) {
                return i;
            }
        }
        return -1;
    }

    private int totalAmount(List<CartItem> carts) {
        int totalAmount = 0;
        for (CartItem cartItem : carts) {
            totalAmount = totalAmount + (cartItem.getQuantity() * cartItem.getProduct().getPrice().intValue());
        }
        return totalAmount;
    }
}
