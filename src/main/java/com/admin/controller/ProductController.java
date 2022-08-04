package com.admin.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.entity.Category;
import com.entity.Color;
import com.entity.Product;
import com.entity.Size;
import com.entity.Supply;
import com.model.SearchModel;
import com.request.ProductRequest;
import com.service.CategoryService;
import com.service.FilesStorageService;
import com.service.ProductService;
import com.service.SupplyService;
import com.util.NewsUtil;

@Controller
@RequestMapping("/admin/product")
public class ProductController {

    private static final String POST_URL = "admin/product";
    private final ProductService productService;
    private final CategoryService categoryService;
    private final FilesStorageService storageService;

    @Autowired
    private SupplyService supplyService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService,
                             FilesStorageService storageService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.storageService = storageService;
    }

    // display list of employees
    @GetMapping
    public String viewList(Model model, HttpSession session, @RequestParam(value = "supplyId", required = false) Integer supplyId,
                           @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        if (!NewsUtil.isLogin(session)) {
            return "redirect:/login";
        }
        List<Product> items;
        SearchModel searchModel = new SearchModel();
        if (supplyId != null) {
            items = new ArrayList<>(supplyService.findById(supplyId).getProducts());
            searchModel.setSupplyId(supplyId);
        } else {

            items = productService.list();
            searchModel.setSupplyId(supplyId);
        }
        Page<Product> productPage = productService.findPaginated(items, PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("items", productPage);
        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("searchItem", searchModel);
        model.addAttribute("supplies", supplyService.list());
        return POST_URL + "/index";
    }

    @GetMapping("/add")
    public String viewAdd(Model model, HttpSession session) {
        if (!NewsUtil.isLogin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("categories", categoryService.list());
        model.addAttribute("supplies", supplyService.list());
        model.addAttribute("item", new ProductRequest());
        return POST_URL + "/add";
    }

    @PostMapping
    public String add(@Valid ProductRequest request, BindingResult result, Model model, HttpSession session) {
        if (!NewsUtil.isLogin(session)) {
            return "redirect:/login";
        }
        if (result.hasErrors()) {
            return POST_URL + "add";
        }

        Product item = new Product();
        item.setPin(request.isPin());
        item.setProductName(request.getProductName());
        item.setDescription(request.getDescription());
        item.setQuantity(request.getQuantity());
        item.setPrice(request.getPrice());

        if (!request.getSize().isEmpty()) {
            Set<Size> sizes = new HashSet<Size>();
            for (int i = 0; i < request.getSize().split(",").length; i++) {
                Size size = new Size(item, request.getSize().split(",")[i]);
                sizes.add(size);
            }
            item.setSizes(sizes);
        }

        if (!request.getColor().isEmpty()) {
            Set<Color> colors = new HashSet<Color>();
            for (int i = 0; i < request.getColor().split(",").length; i++) {
                Color color = new Color(item, request.getColor().split(",")[i]);
                colors.add(color);
            }
            item.setColors(colors);
        }

        Category category = categoryService.findById(request.getCategoryId());
        item.setCategory(category);

        Supply supply = supplyService.findById(request.getSupplyId());
        item.setSupply(supply);
        item.setCreatedDate(new Date());
        if (request.getImage() != null) {
            String fileName = Calendar.getInstance().getTimeInMillis() + "_" + request.getImage().getOriginalFilename();
            storageService.save(request.getImage(), fileName);
            item.setImage(fileName);
        }
        productService.create(item);
        return "redirect:/admin/product";
    }

    @GetMapping("/edit/{id}")
    public String findById(@PathVariable("id") int id, Model model, HttpSession session) {
        if (!NewsUtil.isLogin(session)) {
            return "redirect:/login";
        }
        Product product = productService.findById(id);
        ProductRequest request = new ProductRequest();
        request.setCategoryId(product.getCategory().getId());
        request.setId(id);
        request.setProductName(product.getProductName());
        request.setQuantity(product.getQuantity());
        request.setPrice(product.getPrice());
        request.setDescription(product.getDescription());
        request.setPin(product.isPin());
        request.setImageName(product.getImage());
        model.addAttribute("item", request);
        model.addAttribute("categories", categoryService.list());
        model.addAttribute("supplies", supplyService.list());
        return POST_URL + "/update";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") int id, Model model, HttpSession session) {
        if (!NewsUtil.isLogin(session)) {
            return "redirect:/login";
        }
        Product product = productService.findById(id);

        model.addAttribute("item", product);

        return POST_URL + "/comment";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable("id") int id, @Valid ProductRequest request, BindingResult result, Model model,
                         HttpSession session) {
        if (!NewsUtil.isLogin(session)) {
            return "redirect:/login";
        }
        if (result.hasErrors()) {
            request.setId(id);
            model.addAttribute("post", request);
            return POST_URL + "/update";
        }
        Product product = new Product();
        product.setPin(request.isPin());
        product.setProductName(request.getProductName());
        product.setQuantity(request.getQuantity());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        Category category = categoryService.findById(request.getCategoryId());
        product.setCategory(category);

        Supply supply = supplyService.findById(request.getSupplyId());
        product.setSupply(supply);

        product.setCreatedDate(new Date());
        if (request.getImage() != null) {
            String fileName = request.getImage().getOriginalFilename() + "_" + Calendar.getInstance().getTimeInMillis();
            storageService.save(request.getImage(), fileName);
            product.setImage(fileName);
        } else {
            product.setImage(productService.findById(id).getImage());
        }
        productService.update(id, product);

        return "redirect:/admin/product";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id, Model model, HttpSession session) {
        if (!NewsUtil.isLogin(session)) {
            return "redirect:/login";
        }
        productService.delete(id);
        return "redirect:/admin/product";
    }

    @GetMapping("/search")
    public String viewList1(Model model, HttpSession session, @RequestParam(value = "search", required = false) String search,
                            @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        if (!NewsUtil.isLogin(session)) {
            return "redirect:/login";
        }
        List<Product> items;
        SearchModel searchModel = new SearchModel();
        if (search.trim().equals("")) {
            items = new ArrayList<>(productService.list());

        } else {
            searchModel.setSearch(search);
            items = productService.findByProductName("%" + search + "%");
        }
        Page<Product> productPage = productService.findPaginated(items, PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("items", productPage);
        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("search", search);
        model.addAttribute("searchItem", searchModel);
        model.addAttribute("supplies", supplyService.list());
        return POST_URL + "/index";
    }
}
