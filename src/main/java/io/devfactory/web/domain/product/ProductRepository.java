package io.devfactory.web.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingStatuses);

  List<Product> findAllByProductNumberIn(List<String> productNumbers);

  @Query(value = "select p.product_number from tb_product p order by id desc limit 1", nativeQuery = true)
  String findLatestProductNumber();

}
