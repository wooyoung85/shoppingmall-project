package com.wooyoung85.shoppingmallrestapi.products.controller;

import com.wooyoung85.shoppingmallrestapi.common.BaseControllerTest;
import com.wooyoung85.shoppingmallrestapi.common.TestDescription;
import com.wooyoung85.shoppingmallrestapi.products.domain.Product;
import com.wooyoung85.shoppingmallrestapi.products.domain.ProductItem;
import com.wooyoung85.shoppingmallrestapi.products.domain.ProductSupportType;
import com.wooyoung85.shoppingmallrestapi.products.dto.ProductDto;
import com.wooyoung85.shoppingmallrestapi.products.dto.ProductItemDto;
import com.wooyoung85.shoppingmallrestapi.products.repository.ProductRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProductControllerTest extends BaseControllerTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @TestDescription("정상으로 상품을 생성하는 테스트")
    public void createProduct() throws Exception {
        // Given
        List<ProductItemDto> productItemList = new ArrayList<>();

        ProductItemDto productItem1 = ProductItemDto.builder()
            .color("프리즘 그린")
            .colorCode("#32829D")
            .imageCode("A0ZA_001")
            .quantity(10)
            .build();

        productItemList.add(productItem1);

        ProductDto productDto = ProductDto.builder()
            .brand("삼성")
            .name("갤럭시")
            .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
            .category("핸드폰")
            .productSupportType("welfare")
            .productItems(productItemList)
            .build();

        // When
        mockMvc.perform(post("/api/products/")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaTypes.HAL_JSON)
            .content(objectMapper.writeValueAsString(productDto))
        )
            // Then
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("brand").exists())
            .andExpect(jsonPath("name").exists())
            .andExpect(jsonPath("description").exists())
            .andExpect(jsonPath("category").exists())
            .andExpect(jsonPath("productSupportType").exists())
            .andExpect(jsonPath("createDateTime").exists())
            .andExpect(jsonPath("updateDateTime").exists())
            .andExpect(jsonPath("productItems", hasSize(1)))
            .andExpect(jsonPath("productItems[0].id").exists())
            .andExpect(jsonPath("productItems[0].color").exists())
            .andExpect(jsonPath("productItems[0].colorCode").exists())
            .andExpect(jsonPath("productItems[0].imageCode").exists())
            .andExpect(jsonPath("productItems[0].quantity").exists())
            // Document
            .andDo(document("create-product",
                links(
                    linkWithRel("self").description("link to self"),
                    linkWithRel("query-products").description("link to query products"),
                    linkWithRel("update-product").description("link to update an existing"),
                    linkWithRel("profile").description("link to update an existing")
                ),
                requestHeaders(
                    headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                    headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                ),
                requestFields(
                    fieldWithPath("brand").description("상품 브랜드"),
                    fieldWithPath("name").description("상품명"),
                    fieldWithPath("description").description("date time of beginEnrollmentDateTime"),
                    fieldWithPath("category").description("date time of closeEnrollmentDateTime"),
                    fieldWithPath("productSupportType").description("date time of beginEventDateTime"),
                    fieldWithPath("productItems.[].color").description("date time of endEventDateTime"),
                    fieldWithPath("productItems.[].colorCode").description("date time of endEventDateTime"),
                    fieldWithPath("productItems.[].imageCode").description("date time of endEventDateTime"),
                    fieldWithPath("productItems.[].quantity").description("date time of endEventDateTime"),
                    fieldWithPath("createDateTime").description("date time of endEventDateTime")
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.LOCATION).description("Location header"),
                    headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                ),
                responseFields(
                    fieldWithPath("id").description("identifier of new event"),
                    fieldWithPath("brand").description("Name of new event"),
                    fieldWithPath("name").description("description of new event"),
                    fieldWithPath("description").description("date time of beginEnrollmentDateTime"),
                    fieldWithPath("category").description("date time of closeEnrollmentDateTime"),
                    fieldWithPath("productSupportType").description("date time of beginEventDateTime"),
                    fieldWithPath("productItems[].id").description("date time of endEventDateTime"),
                    fieldWithPath("productItems[].color").description("date time of endEventDateTime"),
                    fieldWithPath("productItems[].colorCode").description("date time of endEventDateTime"),
                    fieldWithPath("productItems[].imageCode").description("date time of endEventDateTime"),
                    fieldWithPath("productItems[].quantity").description("date time of endEventDateTime"),
                    fieldWithPath("createDateTime").description("date time of endEventDateTime"),
                    fieldWithPath("updateDateTime").description("date time of endEventDateTime"),
                    fieldWithPath("_links.self.href").description("link to self"),
                    fieldWithPath("_links.query-products.href").description("link to query products"),
                    fieldWithPath("_links.update-product.href").description("link to update event"),
                    fieldWithPath("_links.profile.href").description("link to profile")
                )
            ))
        ;
    }

    @Test
    @Transactional
    @TestDescription("30개의 상품을 10개씩 두번째 페이지 조회하기")
    public void queryProducts() throws Exception {
        // Given
        IntStream.range(0, 30).forEach(i -> this.generateProduct(i));

        // When
        this.mockMvc.perform(get("/api/products")
            .param("page", "1")
            .param("size", "10")
            .param("sort", "name,DESC")
        )
            // Then
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("page").exists())
            .andExpect(jsonPath("_embedded.productList", hasSize(10)))
            .andExpect(jsonPath("_embedded.productList[0]._links.self").exists())
            .andExpect(jsonPath("_links.first").exists())
            .andExpect(jsonPath("_links.self").exists())
            .andExpect(jsonPath("_links.next").exists())
            .andExpect(jsonPath("_links.last").exists())
            .andExpect(jsonPath("_links.profile").exists())
            // Document
            .andDo(document("query-products",
                links(
                    linkWithRel("self").description("현재 페이지 링크 url"),
                    linkWithRel("first").description("첫번째 페이지 링크 url"),
                    linkWithRel("prev").description("이전 페이지 링크 url"),
                    linkWithRel("profile").description("link to self"),
                    linkWithRel("next").description("다음 페이지 링크 url"),
                    linkWithRel("last").description("마지막 페이지 링크 url")
                ),
                requestParameters(
                    parameterWithName("page").description("페이지 인덱스"),
                    parameterWithName("size").description("페이지 크기"),
                    parameterWithName("sort").description("상품 정렬 기준")

                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                ),
                responseFields(
                    fieldWithPath("_embedded.productList[].id").description("상품 아이디"),
                    fieldWithPath("_embedded.productList[].brand").description("상품 브랜드명"),
                    fieldWithPath("_embedded.productList[].name").description("상품명"),
                    fieldWithPath("_embedded.productList[].description").description("상품 설명"),
                    fieldWithPath("_embedded.productList[].category").description("상품 카테고리"),
                    fieldWithPath("_embedded.productList[].productSupportType").description("복지혜택 지원 유형"),
                    fieldWithPath("_embedded.productList[].createDateTime").description("생성시간"),
                    fieldWithPath("_embedded.productList[].updateDateTime").description("업데이트시간"),
                    fieldWithPath("_embedded.productList[].productItems[].id").description("상품 아이템 아이디"),
                    fieldWithPath("_embedded.productList[].productItems[].color").description("상품 아이템 색상명"),
                    fieldWithPath("_embedded.productList[].productItems[].colorCode").description("상품 아이템 색상 코드"),
                    fieldWithPath("_embedded.productList[].productItems[].imageCode").description("상품 아이템 이미지 코드"),
                    fieldWithPath("_embedded.productList[].productItems[].quantity").description("상품 아이템 수량"),
                    fieldWithPath("_embedded.productList[]._links.self.href").description("상품 링크 url"),
                    fieldWithPath("_links.first.href").description("첫번째 페이지 링크 url"),
                    fieldWithPath("_links.prev.href").description("이전 페이지 링크 url"),
                    fieldWithPath("_links.self.href").description("현재 페이지 링크 url"),
                    fieldWithPath("_links.next.href").description("다음 페이지 링크 url"),
                    fieldWithPath("_links.last.href").description("마지막 페이지 링크 url"),
                    fieldWithPath("_links.profile.href").description("link to self"),
                    fieldWithPath("page.size").description("페이지 크기"),
                    fieldWithPath("page.totalElements").description("전체 상품 갯수"),
                    fieldWithPath("page.totalPages").description("전체 페이지 수"),
                    fieldWithPath("page.number").description("현재 페이지 번호")
                )
            ))
        ;
    }

    private void generateProduct(int i) {
        Product product = new Product();
        ProductItem productItem = new ProductItem();
        productItem.setColor("color" + i);
        productItem.setColorCode("colorCode");
        productItem.setImageCode("ImageCode");
        productItem.setQuantity(i);
        productItem.setProduct(product);

        product.setBrand("brand" + i);
        product.setName("name" + i);
        product.setDescription("description" + i);
        product.setCategory("category" + i);
        product.setProductSupportType(ProductSupportType.NONE);
        product.setCreateDateTime(LocalDateTime.now());
        product.setProductItems(new ArrayList<>(Arrays.asList(productItem)));

        this.productRepository.save(product);
    }
}