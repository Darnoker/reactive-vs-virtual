package pl.edu.ug.kglab.ReactiveTestApp.orderInfo;

import lombok.Data;
import pl.edu.ug.kglab.ReactiveTestApp.user.model.User;

import java.util.List;

@Data
public class UserProductInfo {

    private String userId;

    private String name;

    private String lastname;

    private String email;

    private List<ProductInfo> productInfos;
}
