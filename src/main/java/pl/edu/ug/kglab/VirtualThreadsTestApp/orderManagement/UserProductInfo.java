package pl.edu.ug.kglab.VirtualThreadsTestApp.orderManagement;

import lombok.Data;

import java.util.List;

@Data
public class UserProductInfo {

    private String userId;

    private String name;

    private String lastname;

    private String email;

    private List<ProductInfo> productInfos;
}
