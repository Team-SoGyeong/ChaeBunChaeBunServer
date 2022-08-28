# 채분채분 [![Build Status](https://travis-ci.com/Team-SoGyeong/ChaeBunChaeBunServer.svg?branch=master)](https://travis-ci.com/Team-SoGyeong/ChaeBunChaeBunServer)

## 🥦 서비스 소개
신선한 채소를 적당량만 먹기 위한 근거리 채소 소분 어플

- 기본 기능: 채분팟 모집, 채분팟 참여

## 🛠 dependencies
````
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-validation'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	compileOnly 'org.projectlombok:lombok'
	runtimeOnly 'org.mariadb.jdbc:mariadb-java-client'
	annotationProcessor 'org.projectlombok:lombok'
	implementation group: 'io.springfox', name: 'springfox-swagger-ui', version: '2.9.2'
	implementation group: 'io.springfox', name: 'springfox-swagger2', version: '2.9.2'
	implementation group: 'io.jsonwebtoken', name: 'jjwt', version: '0.9.1'
	testImplementation('org.springframework.boot:spring-boot-starter-test') {
		exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
	}
	implementation group: 'org.springframework.cloud', name: 'spring-cloud-starter-aws', version: '2.2.1.RELEASE'
}
````

## links
📌 [APIs](http://3.37.243.188:8080/swagger-ui.html)<br>
📌 [플레이스토어](https://play.google.com/store/apps/details?id=com.E2I3.chaebunchaebun)
