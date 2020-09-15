package com.github.doobo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SingleObj {
    
    private String address="www.5fu8.com";
    
    private String author="doobo";
}
