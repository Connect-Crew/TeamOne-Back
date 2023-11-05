package com.connectcrew.teamone.api.project.values;

import com.connectcrew.teamone.api.project.NameKey;
import com.connectcrew.teamone.api.project.NameKeyValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum MemberPart {
    TOTAL_DEVELOP(MemberPartCategory.DEVELOP, "개발 전체"),
    AOS(MemberPartCategory.DEVELOP, "AOS 개발자"),
    IOS(MemberPartCategory.DEVELOP, "IOS 개발자"),
    FRONTEND(MemberPartCategory.DEVELOP, "프론트엔드 개발자"),
    BACKEND(MemberPartCategory.DEVELOP, "백엔드 개발자"),
    CROSS_PLATFORM(MemberPartCategory.DEVELOP, "크로스 플랫폼 개발자"),
    DEVOPS(MemberPartCategory.DEVELOP, "DevOps"),
    QA(MemberPartCategory.DEVELOP, "QA"),
    GAME(MemberPartCategory.DEVELOP, "게임 개발자"),
    GRAPHIC_ENGINEER(MemberPartCategory.DEVELOP, "그래픽 엔지니어"),
    DATA_ENGINEER(MemberPartCategory.DEVELOP, "데이터 엔지니어"),
    VIDEO_SOUND_ENGINEER(MemberPartCategory.DEVELOP, "영상음성 엔지니어"),
    WEB_PUBLISHER(MemberPartCategory.DEVELOP, "웹 퍼블리셔"),
    EMBEDDED(MemberPartCategory.DEVELOP, "임베디드 개발자"),
    SECURITY(MemberPartCategory.DEVELOP, "보안 엔지니어"),
    NETWORK(MemberPartCategory.DEVELOP, "네트워크 관리자"),


    TOTAL_MANAGER(MemberPartCategory.MANAGER, "기획 전체"),
    PL_PM_PO(MemberPartCategory.MANAGER, "PL·PM·PO"),
    MANAGEMENT_BUSINESS_MANAGER(MemberPartCategory.MANAGER, "경영·비즈니스 기획"),
    APP_MANAGER(MemberPartCategory.MANAGER, "앱 기획자"),
    WEB_MANAGER(MemberPartCategory.MANAGER, "웹 기획자"),
    MARKETING_MANAGER(MemberPartCategory.MANAGER, "마케팅 기획자"),
    BUSINESS_DEVELOP_MANAGER(MemberPartCategory.MANAGER, "사업개발·기획자"),
    SERVICE_MANAGER(MemberPartCategory.MANAGER, "서비스 기획자"),
    OPERATION_MANAGER(MemberPartCategory.MANAGER, "운영 매니저"),
    STRATEGY_MANAGER(MemberPartCategory.MANAGER, "전략 기획자"),
    CONSULTANT(MemberPartCategory.MANAGER, "컨설턴트"),


    TOTAL_DESIGN(MemberPartCategory.DESIGN, "디자인 전체"),
    UI_UX_DESIGNER(MemberPartCategory.DESIGN, "UI·UX 디자이너"),
    APP_DESIGNER(MemberPartCategory.DESIGN, "앱 디자이너"),
    WEB_DESIGNER(MemberPartCategory.DESIGN, "웹 디자이너"),
    GRAPHIC_DESIGNER(MemberPartCategory.DESIGN, "그래픽 디자이너"),
    PRODUCT_DESIGNER(MemberPartCategory.DESIGN, "제품 디자이너"),
    ADVERTISEMENT_DESIGNER(MemberPartCategory.DESIGN, "광고 디자이너"),
    DESIGNER_2D(MemberPartCategory.DESIGN, "2D 디자이너"),
    DESIGNER_3D(MemberPartCategory.DESIGN, "3D 디자이너"),
    VIDEO_MOTION_DESIGNER(MemberPartCategory.DESIGN, "영상·모션 디자이너"),
    BI_BX_DESIGNER(MemberPartCategory.DESIGN, "BI·BX 디자이너"),
    FASHION_DESIGNER(MemberPartCategory.DESIGN, "패션 디자이너"),
    CHARACTER_DESIGNER(MemberPartCategory.DESIGN, "캐릭터 디자이너"),
    SPACE_DESIGNER(MemberPartCategory.DESIGN, "공간 디자이너"),


    TOTAL_MARKETING(MemberPartCategory.MARKETING, "마케팅 전체"),
    MARKETING(MemberPartCategory.MARKETING, "마케터"),
    DEGITAL_MARKETING(MemberPartCategory.MARKETING, "디지털 마케터"),
    CONTENT_MARKETING(MemberPartCategory.MARKETING, "콘텐츠 마케터"),
    PERFORMANCE_MARKETING(MemberPartCategory.MARKETING, "퍼포먼스 마케터"),
    BRAND_MARKETING(MemberPartCategory.MARKETING, "브랜드 마케터"),
    MARKETING_STRATEGY_MANAGER(MemberPartCategory.MARKETING, "마케팅 전략 기획자"),
    ADVERTISEMENT_MANAGER(MemberPartCategory.MARKETING, "광고 기획자"),
    GLOBAL_MARKETING(MemberPartCategory.MARKETING, "글로벌 마케터"),
    PR(MemberPartCategory.MARKETING, "PR"),
    COPYWRITER(MemberPartCategory.MARKETING, "카피라이터"),
    ALLIANCE_MARKETING(MemberPartCategory.MARKETING, "제휴 마케터"),
    MARKET_RESEARCH(MemberPartCategory.MARKETING, "마켓 리서치"),


    TOTAL_SALES(MemberPartCategory.SALES, "영업 전체"),
    SALES_MANAGER(MemberPartCategory.SALES, "영업 관리"),
    SALES_SUPPORT(MemberPartCategory.SALES, "영업 지원"),
    IT_TECHNICAL_SALES(MemberPartCategory.SALES, "IT·기술 영업"),
    PRODUCT_SALES(MemberPartCategory.SALES, "제품 영업"),
    SERVICE_SALES(MemberPartCategory.SALES, "서비스 영업"),
    FOREIGN_SALES(MemberPartCategory.SALES, "해외 영업"),
    ADVERTISEMENT_SALES(MemberPartCategory.SALES, "광고 영업"),
    FINANCIAL_SALES(MemberPartCategory.SALES, "금융 영업"),
    CORPORATION_SALES(MemberPartCategory.SALES, "법인 영업"),


    TOTAL_CUSTOMER(MemberPartCategory.CS, "고객서비스 전체"),
    AS_TECHNICIAN(MemberPartCategory.CS, "AS 기술자"),
    CS_MANAGER(MemberPartCategory.CS, "CS 매니저"),
    RETAIL_MD(MemberPartCategory.CS, "리테일 MD"),
    FASHION_MD(MemberPartCategory.CS, "패션 MD"),
    STORE_MANAGER(MemberPartCategory.CS, "매장 관리자"),
    FRANCHISEE_MANAGER(MemberPartCategory.CS, "가맹점 관리자"),
    TELEMARKETER(MemberPartCategory.CS, "텔레마케터"),
    RECEPTION(MemberPartCategory.CS, "리셉션"),
    STEWARDESS(MemberPartCategory.CS, "승무원"),
    TRAVEL_AGENT(MemberPartCategory.CS, "여행 에이전트"),
    BEAUTY_MANAGER(MemberPartCategory.CS, "미용 관리사"),


    TOTAL_SPECIAL(MemberPartCategory.SPECIALIST, "전문직 전체"),
    EDUCATION(MemberPartCategory.SPECIALIST, "교육"),
    LEGAL_LABOR(MemberPartCategory.SPECIALIST, "법률·노무"),
    MEDICAL_BIO(MemberPartCategory.SPECIALIST, "의료·바이오"),
    RESTAURANT_CHEF(MemberPartCategory.SPECIALIST, "요식업·쉐프"),


    TOTAL_ENGINEER(MemberPartCategory.ENGINEER, "엔지니어 전체"),
    SEMICONDUCTOR_ENGINEER(MemberPartCategory.ENGINEER, "반도체 엔지니어"),
    BATTERY_ELECTRICS_ENGINEER(MemberPartCategory.ENGINEER, "전지·전자 엔지니어"),
    MECHANICAL_ENGINEER(MemberPartCategory.ENGINEER, "기계 엔지니어"),
    DESIGN_ENGINEER(MemberPartCategory.ENGINEER, "설계 엔지니어"),
    FACILITY_ENGINEER(MemberPartCategory.ENGINEER, "설비 엔지니어"),
    CHEMICAL_ENGINEER(MemberPartCategory.ENGINEER, "화학 엔지니어"),
    HARDWARE_ENGINEER(MemberPartCategory.ENGINEER, "하드웨어 엔지니어"),
    TELECOMMUNICATIONS_ENGINEER(MemberPartCategory.ENGINEER, "통신 엔지니어"),
    PROCESS_ENGINEER(MemberPartCategory.ENGINEER, "공정 엔지니어"),
    RF_ENGINEER(MemberPartCategory.ENGINEER, "RF 엔지니어"),
    FIELD_ENGINEER(MemberPartCategory.ENGINEER, "필드 엔지니어"),
    R_AND_D_ENGINEER(MemberPartCategory.ENGINEER, "R&D 엔지니어"),

    TOTAL_MEDIA(MemberPartCategory.MEDIA, "미디어 전체"),
    PD_DIRECTOR(MemberPartCategory.MEDIA, "PD·감독"),
    REPORTER(MemberPartCategory.MEDIA, "리포터"),
    INFLUENCER(MemberPartCategory.MEDIA, "인플루언서"),
    CONTENT_CREATOR(MemberPartCategory.MEDIA, "콘텐츠 크리에이터"),
    PHOTOGRAPHER(MemberPartCategory.MEDIA, "포토그래퍼"),
    VIDEO_EDITOR(MemberPartCategory.MEDIA, "영상 편집자"),
    SOUND_ENGINEER(MemberPartCategory.MEDIA, "음향 엔지니어"),
    ENTERTAINMENT(MemberPartCategory.MEDIA, "연예·엔터테인먼트"),
    PUBLISHER_EDITOR(MemberPartCategory.MEDIA, "출판·편집"),
    DISTRIBUTION_PRODUCER(MemberPartCategory.MEDIA, "배급·제작자"),
    STAFF(MemberPartCategory.MEDIA, "스태프"),

    ETC(MemberPartCategory.ETC, "기타");

    private final MemberPartCategory category;
    private final String description;

    public static List<NameKeyValue<NameKey>> getNameKeyValues() {
        List<NameKeyValue<NameKey>> result = new ArrayList<>();

        Map<MemberPartCategory, List<NameKey>> partMap = new HashMap<>();
        for (MemberPart part : MemberPart.values()) {
            partMap.computeIfAbsent(part.category, k -> new ArrayList<>()).add(new NameKey(part.description, part.name()));
        }

        for (MemberPartCategory category : partMap.keySet()) {
            result.add(new NameKeyValue<>(category.getDescription(), category.name(), partMap.get(category)));
        }

        return result;
    }
}
