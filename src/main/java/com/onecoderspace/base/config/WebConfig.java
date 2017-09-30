package com.onecoderspace.base.config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.ExportMetricWriter;
import org.springframework.boot.actuate.endpoint.MetricReaderPublicMetrics;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.metrics.aggregate.AggregateMetricReader;
import org.springframework.boot.actuate.metrics.export.MetricExportProperties;
import org.springframework.boot.actuate.metrics.reader.MetricReader;
import org.springframework.boot.actuate.metrics.repository.redis.RedisMetricRepository;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.google.common.collect.Maps;
import com.onecoderspace.base.filter.CsrfFilter;
import com.onecoderspace.base.filter.ShiroSessionFilter;
import com.onecoderspace.base.filter.XssFilter;

@Configuration
public class WebConfig {

	@Autowired
	private RedisConnectionFactory redisConnectionFactory;
	
	@Value("${server.session.timeout}")
	private String serverSessionTimeout;
	
	@Value("${csrf.filter.open}")
	private String isCsrfFilterOpen;

	@Bean
	@ExportMetricWriter
	MetricWriter metricWriter(MetricExportProperties export) {
		return new RedisMetricRepository(redisConnectionFactory, export
				.getRedis().getPrefix(), export.getRedis().getKey());
	}

	@Autowired
	private MetricExportProperties export;

	@Bean
	public PublicMetrics metricsAggregate() {
		return new MetricReaderPublicMetrics(aggregatesMetricReader());
	}

	private MetricReader globalMetricsForAggregation() {
		return new RedisMetricRepository(this.redisConnectionFactory,
				this.export.getRedis().getAggregatePrefix(), this.export
						.getRedis().getKey());
	}

	private MetricReader aggregatesMetricReader() {
		AggregateMetricReader repository = new AggregateMetricReader(
				globalMetricsForAggregation());
		return repository;
	}
	
	/**
	 * 参数绑定时将String时间转换为Date
	 * @author yangwk
	 * @time 2017年7月26日 下午4:53:19
	 * @return
	 */
	@Bean
    public Converter<String, Date> stringDateConvert() {
        return new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = sdf.parse((String) source);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return date;
            }
        };
    }

	/**
	 * 解决跨域调用问题
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*")
						.allowedMethods("*").allowedHeaders("*")
						.allowCredentials(true)
						.exposedHeaders(HttpHeaders.SET_COOKIE).maxAge(3600L);
			}
		};
	}

	/**
	 * xss过滤拦截器
	 */
	@Bean
	public FilterRegistrationBean xssFilterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new XssFilter());
		filterRegistrationBean.setOrder(1);
		filterRegistrationBean.setEnabled(true);
		filterRegistrationBean.addUrlPatterns("/*");
		Map<String, String> initParameters = Maps.newHashMap();
		initParameters.put("excludes", "/favicon.ico,/img/*,/js/*,/css/*");
		initParameters.put("isIncludeRichText", "true");
		filterRegistrationBean.setInitParameters(initParameters);
		return filterRegistrationBean;
	}

	/**
	 * csrf过滤拦截器 只处理post请求
	 */
	@Bean
	public FilterRegistrationBean csrfFilterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new CsrfFilter());
		filterRegistrationBean.setOrder(2);
		filterRegistrationBean.setEnabled(true);
		filterRegistrationBean.addUrlPatterns("/*");
		Map<String, String> initParameters = Maps.newHashMap();
		initParameters.put("excludes", "/login/*");
		initParameters.put("isOpen", isCsrfFilterOpen);
		filterRegistrationBean.setInitParameters(initParameters);
		return filterRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean shiroSessionFilterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new ShiroSessionFilter());
		filterRegistrationBean.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
		filterRegistrationBean.setEnabled(true);
		filterRegistrationBean.addUrlPatterns("/*");
		Map<String, String> initParameters = Maps.newHashMap();
		initParameters.put("serverSessionTimeout", serverSessionTimeout);
		initParameters.put("excludes", "/favicon.ico,/img/*,/js/*,/css/*");
		filterRegistrationBean.setInitParameters(initParameters);
		return filterRegistrationBean;
	}

}
