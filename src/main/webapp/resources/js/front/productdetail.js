$(function() {
	var productId = getQueryString('productId');
	var productUrl = '/o2o/front/listproductdetailpageinfo?productId=' + productId;

	$.getJSON(
		productUrl,function(data) {
			if (data.success) {
				var product = data.product;
				//给商品相关控件HTML赋值
				
				//商品缩略图
				$('#product-img').attr('src', getContextPath()+product.imgAddr);
				//商品更新时间
				$('#product-time').text(new Date(product.lastEditTime) .Format("yyyy-MM-dd"));
				
				//显示积分
				$('#product-point').text('购买可得'+product.point+'积分');
				//商品名字
				$('#product-name').text(product.productName);
				//商品描述
				$('#product-desc').text(product.productDesc);
				// 获取商品详情图片列表
				var productDetailImgList = product.productImgList;
				var swiperHtml = '';
				productDetailImgList.map(function(item, index) {
					swiperHtml += ''
                        + '<div class="swiper-slide img-wrap">'
                        +      '<img class="banner-img" src="'+getContextPath()+ item.imgAddr +'" alt="'+ item.imgDesc +'">'
                        + '</div>';
				});
				// 生成购买商品的二维码供商家扫描
//				imgListHtml += '<div> <img src="/o2o/front/generateqrcode4product?productId=' + product.productId + '"/></div>';
				$('.swiper-wrapper').html(swiperHtml);
				 // 设置轮播图轮换时间为1秒
	            $(".swiper-container").swiper({
	                autoplay: 1000,
	                // 用户对轮播图进行操作时，是否自动停止autoplay
	                autoplayDisableOnInteraction: true
	            });
			}
		});
	$('#me').click(function() {
		$.openPanel('#panel-left-demo');
	});
	$.init();
});
