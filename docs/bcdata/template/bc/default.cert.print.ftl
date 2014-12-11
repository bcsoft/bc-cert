<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>证件打印</title>
	<style type="text/css">
		div{ color: red; font-weight: bold;}
		img{ margin: 0px;}
		.page-break-after{display:block;page-break-after:always;}
		.page-break-before{display:block;page-break-before:always;}
		<#list certs as m0>
			<#if m0_index == 0>
				<#if m0['print_direction'] == 0>
					@page {
						size: A4 portrait;
					}
				<#else>
					@page {
						size: A4 landscape;
					}
				</#if>
			</#if>
		</#list>
	</style>
</head>
<body>
<#assign one_page_one_typography=100>
<#if certs?size == 0><div>找不到符合条件的证件</div></#if>
<#list certs as m>
	
	<#if m['attachId']??>
		
			<#if m['print_direction'] == 0 > <!-- 纵向打印 --> 
					<#if m['one_page_one_typography'] == 0 && m_index != certs?size -1 && one_page_one_typography == 0>
						<!-- 证件宽度过大 设置了一页一版，不是最后一条数据，并且上一条数据也设置了一页一版(这里不包括只有一条数据的情况，因为默认第一条one_page_one_typography = 100) -->
							<img src="${htmlPageNamespace}/bc/image/download?id=${m['attachId']}" style="width:${m['attachWidth']?number * 0.9390}mm;max-width: calc(186mm  * 0.9390);max-height: calc(277mm * 0.9390 );" class="page-break-after">
					<#elseif m['one_page_one_typography'] == 0 && m_index != certs?size -1 && one_page_one_typography == 1>
						<!-- 证件宽度过大 设置了一页一版，不是最后一条数据，并且上一条数据没有设置一页一版(这里不包括只有一条数据的情况 -->	
							<img src="${htmlPageNamespace}/bc/image/download?id=${m['attachId']}" style="width:${m['attachWidth']?number * 0.9390}mm;max-width: calc(186mm  * 0.9390);max-height: calc(277mm * 0.9390 );" class="page-break-after page-break-before">
					<#elseif  m['one_page_one_typography'] == 0 && m_index == certs?size -1 && (m_index > 0)  && one_page_one_typography == 1 >
						<!-- 证件宽度过大 设置了一页一版，是最后一条，并且在不止一条数据的前提下,上一条又不是设置了一页一版 -->
							<img src="${htmlPageNamespace}/bc/image/download?id=${m['attachId']}" style="width:${m['attachWidth']?number * 0.9390}mm;max-width: calc(186mm  * 0.9390);max-height: calc(277mm * 0.9390 );" class="page-break-before">
					<#elseif  m['one_page_one_typography'] == 0 && m_index == 0 && (certs?size > 1)>
						<!-- 证件宽度过大 设置了一页一版，证件数据大于1的时候的第一条 -->
							<img src="${htmlPageNamespace}/bc/image/download?id=${m['attachId']}" style="width:${m['attachWidth']?number * 0.9390}mm;max-width: calc(186mm  * 0.9390);max-height: calc(277mm * 0.9390 );"  class="page-break-after">
					<#else>
						<!-- 证件宽度过大其他情况下 -->
							<img src="${htmlPageNamespace}/bc/image/download?id=${m['attachId']}" style="width:${m['attachWidth']?number * 0.9390}mm;max-width: calc(186mm  * 0.9390);max-height: calc(277mm * 0.9390 );" >	
					</#if>
			<#else><!-- 横向打印 -->

				<#if m['one_page_one_typography'] == 0 && m_index != certs?size -1 && one_page_one_typography == 0>
					<!-- 设置了一页一版，不是最后一条数据，并且上一条数据也设置了一页一版(这里不包括只有一条数据的情况，因为默认第一条one_page_one_typography = 100) -->
						<img src="${htmlPageNamespace}/bc/image/download?id=${m['attachId']}" style="width:${m['attachWidth']?number * 0.9390}mm;max-width: calc(277mm  * 0.9390);max-height: calc(186mm * 0.9390 );" class="page-break-after">
				<#elseif m['one_page_one_typography'] == 0 && m_index != certs?size -1 && one_page_one_typography == 1>
					<!-- 设置了一页一版，不是最后一条数据，并且上一条数据没有设置一页一版(这里不包括只有一条数据的情况 -->	
						<img src="${htmlPageNamespace}/bc/image/download?id=${m['attachId']}" style="width:${m['attachWidth']?number * 0.9390}mm;max-width: calc(277mm  * 0.9390);max-height: calc(186mm * 0.9390 );" class="page-break-after page-break-before">
				<#elseif  m['one_page_one_typography'] == 0 && m_index == certs?size -1 && (m_index > 0)  && one_page_one_typography == 1 >
					<!-- 设置了一页一版，是最后一条，并且在不止一条数据的前提下,上一条又不是设置了一页一版 -->
						<img src="${htmlPageNamespace}/bc/image/download?id=${m['attachId']}" style="width:${m['attachWidth']?number * 0.9390}mm;max-width: calc(277mm  * 0.9390);max-height: calc(186mm * 0.9390 );" class="page-break-before">
				<#elseif  m['one_page_one_typography'] == 0 && m_index == 0 && (certs?size > 1)>
					<!-- 设置了一页一版，证件数据大于1的时候的第一条 -->
						<img src="${htmlPageNamespace}/bc/image/download?id=${m['attachId']}" style="width:${m['attachWidth']?number * 0.9390}mm;max-width: calc(277mm  * 0.9390);max-height: calc(186mm * 0.9390 );" class="page-break-after">
				<#else>
					<!-- 其他情况下 -->
						<img src="${htmlPageNamespace}/bc/image/download?id=${m['attachId']}" style="width:${m['attachWidth']?number * 0.9390}mm;max-width: calc(277mm  * 0.9390);max-height: calc(186mm * 0.9390 );">	
				</#if>

			</#if>
	<#else>
		<div>${m['subject']} 未上传</div>
	</#if>
	<#assign one_page_one_typography= m['one_page_one_typography']>
</#list>
</body>
</html>