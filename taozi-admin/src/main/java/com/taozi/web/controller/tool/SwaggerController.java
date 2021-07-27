package com.taozi.web.controller.tool;

import com.taozi.common.core.controller.BaseController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * swagger 接口
 *
 * @author taozi
 */
@Controller
@RequestMapping("/tool/swagger")
public class SwaggerController extends BaseController {
	@PreAuthorize("@ss.hasPermi('tool:swagger:view')")
	@GetMapping()
	public String index() {
		return redirect("/swagger-ui.html");
	}
}
