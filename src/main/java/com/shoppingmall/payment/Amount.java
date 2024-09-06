package com.shoppingmall.payment;

import lombok.Data;

@Data
public class Amount {
  private Integer tax_free;
  private Integer vat;
  private Integer point;
  private Integer discount;
  private Integer total;
}
