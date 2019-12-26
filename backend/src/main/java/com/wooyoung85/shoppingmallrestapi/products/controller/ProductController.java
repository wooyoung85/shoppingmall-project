package com.wooyoung85.shoppingmallrestapi.products.controller;

import com.wooyoung85.shoppingmallrestapi.accounts.domain.Account;
import com.wooyoung85.shoppingmallrestapi.common.BaseController;
import com.wooyoung85.shoppingmallrestapi.common.CurrentUser;
import com.wooyoung85.shoppingmallrestapi.common.ErrorResource;
import com.wooyoung85.shoppingmallrestapi.events.Event;
import com.wooyoung85.shoppingmallrestapi.events.EventController;
import com.wooyoung85.shoppingmallrestapi.events.EventResource;
import com.wooyoung85.shoppingmallrestapi.products.domain.Product;
import com.wooyoung85.shoppingmallrestapi.products.domain.ProductItem;
import com.wooyoung85.shoppingmallrestapi.products.domain.ProductSupportType;
import com.wooyoung85.shoppingmallrestapi.products.dto.ProductDto;
import com.wooyoung85.shoppingmallrestapi.products.repository.ProductRepository;
import com.wooyoung85.shoppingmallrestapi.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/api/products", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class ProductController extends BaseController {

    private final ProductRepository productRepository;

    private final ProductService productService;

    private final ModelMapper modelMapper;

    private final ProductValidator productValidator;

    @PostMapping
    public ResponseEntity createProduct(@RequestBody @Valid ProductDto productDto,
                                        Errors errors) {
        // 에러체크
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        // 입력값 Validation 검증
        productValidator.validate(productDto, errors);

        // Dto Convert
        Product product = convertProductDto(productDto);

        // 저장
        Long createdProductId = productService.createProduct(product);

        // Response 생성
        ControllerLinkBuilder selfLinkBuilder = linkTo(ProductController.class).slash(createdProductId);
        URI createUri = selfLinkBuilder.toUri();
        ProductResource productResource = new ProductResource(product);
        productResource.add(linkTo(ProductController.class).withRel("query-products"));
        productResource.add(selfLinkBuilder.withRel("update-product"));
        productResource.add(new Link("/docs/index.html#resources-product-create").withRel("profile"));
        return ResponseEntity.created(createUri).body(productResource);
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable,
                                      PagedResourcesAssembler<Product> assembler,
                                      @CurrentUser Account currentUser) {
        Page<Product> page = this.productRepository.findAll(pageable);
        var pagedResources = assembler.toResource(page, e -> new ProductResource(e));
        pagedResources.add(new Link("/docs/index.html#resources-products-query").withRel("profile"));

        if (currentUser != null) {
            pagedResources.add(linkTo(ProductController.class).withRel("create-product"));
        }
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id, @CurrentUser Account currentUser) {
        Optional<Product> optionalProduct = this.productRepository.findById(id);

        if (optionalProduct.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Product product = optionalProduct.get();
        ProductResource productResource = new ProductResource(product);
        productResource.add(new Link("/docs/index.html#resources-products-get").withRel("profile"));
//        if (currentUser != null && product.getManager().equals(currentUser)) {
//            eventResource.add(linkTo(EventController.class).slash(event.getId()).withRel("update-event"));
//        }
        return ResponseEntity.ok(productResource);
    }

    private Product convertProductDto(ProductDto productDto) {
        if (productDto.getCreateDateTime() == null) {
            productDto.setCreateDateTime(LocalDateTime.now());
        }

        Product product = modelMapper.map(productDto, Product.class);
        ProductSupportType productSupportType = getProductSupportType(productDto.getProductSupportType());
        product.setProductSupportType(productSupportType);

        for (ProductItem productItem : product.getProductItems()) {
            productItem.setProduct(product);
        }

        product.setUpdateDateTime(product.getCreateDateTime());

        return product;
    }


    public ProductSupportType getProductSupportType(String productSupportType) {
        for (ProductSupportType r : ProductSupportType.values()) {
            if (r.toString().equals(productSupportType.toUpperCase())) {
                return r;
            }
        }
        return ProductSupportType.NONE;
    }
}
