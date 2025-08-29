# ILKAN-🦁멋쟁이사자차럼 해커톤🦁

<img width="1920" height="1080" alt="표지" src="https://github.com/user-attachments/assets/8705b640-4643-499c-b734-398180867783" />
<img width="100%" height="100%" alt="로고" src="https://github.com/user-attachments/assets/c0a42894-a3ef-453d-b99b-887bc12bf3f2" />

## 🔗 https://ilkan.co.kr/

## 👨‍💻 Team

|                                                            P&D                                                            |                                                           FE                                                            |                                                             FE                                                              |                                                          FE                                                           |                                                          BE                                                           |                                                        BE                                                         |
| :-----------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------: |
| <img src="https://avatars.githubusercontent.com/kimsoyun112" height="100"/> <br> [김소윤](https://github.com/kimsoyun112) | <img src="https://avatars.githubusercontent.com/moonchanju" height="100"/> <br> [문찬주](https://github.com/moonchanju) | <img src="https://avatars.githubusercontent.com/cryingdryice" height="100"/> <br> 👑[박지원](https://github.com/cryingdryice)👑 | <img src="https://avatars.githubusercontent.com/nyeonseok" height="100"/> <br> [안현석](https://github.com/nyeonseok) | <img src="https://avatars.githubusercontent.com/KDWorld81" height="100"/> <br> [곽동욱](https://github.com/KDWorld81) | <img src="https://avatars.githubusercontent.com/lnahyun" height="100"/> <br> [이나현](https://github.com/lnahyun) |

<br>

## 📖 서비스 소개 및 문제 정의

<img width="1920" height="1080" alt="개요" src="https://github.com/user-attachments/assets/1bfa9f0b-d775-4a24-91f7-4696edfc3358" />

---

## ⚙️ 핵심 기능 및 해결 방식

<img width="1920" height="1080" alt="서비스 흐름도" src="https://github.com/user-attachments/assets/93b91435-1f35-480c-b3c8-30a405335061" />
<img width="1920" height="1080" alt="주요기능" src="https://github.com/user-attachments/assets/c0a5e898-e9fd-485d-bba3-3ece569f8d71" />

<br>

### 👤 전문가/창작자
- 프리랜서 공고 확인 → 지원 → 매칭 → 공간 예약까지
- 프로젝트 목적에 맞는 **단기·유연한 공간 확보**

### 🏢 건물주
- **간편 공실 등록**으로 수익화
- 장기 방치 대신 단기 활용을 통한 **새로운 수익 창출**
- 플랫폼 내 대시보드에서 공실 관리

### 🤝 의뢰자
- **적합한 전문가 + 적합한 공간**을 동시에 확보
- 프로젝트 품질 향상 및 관리 부담 감소

---

## 🤖 AI 활용 및 실행 전략

<img width="1920" height="1080" alt="AI 설명" src="https://github.com/user-attachments/assets/9967efa7-11f3-4ae2-a46a-dcf858dd94bd" />

<br>

> 🏳️ 일칸은 **AI 기반 가상 리모델링 어시스턴트**를 도입했습니다.  
> 🏴 건물주는 공실 사진과 요구사항을 입력하면, AI가 구조와 니즈를 분석해 **공유오피스, 스튜디오, 팝업스토어 등 다양한 활용 콘셉트**를 이미지로 제안합니다.  

---


## 📐 Architecture

<img width="868" height="735" alt="백엔드아키텍처 게시용" src="https://github.com/user-attachments/assets/a0191554-79ba-439b-8eeb-1b0f09888320" />
<img width="868" height="736" alt="프론트아키텍처 게시용" src="https://github.com/user-attachments/assets/31ec4c82-a1d1-44d0-8db9-5bfa74a6f104" />

---

## 👋 Commit Message 규칙

| **메시지 타입** | **설명**                                                    |
| --------------- | ----------------------------------------------------------- |
| **feat**        | ✨ 새로운 기능 추가 및 기존 기능 수정                       |
| **fix**         | 🐛 버그 수정                                                |
| **docs**        | 📚 문서 및 주석 수정                                        |
| **style**       | 🎨 코드 스타일 및 포맷팅 수정                               |
| **refact**      | ♻️ 기능 변화 없는 코드 리팩터링                             |
| **test**        | ✅ 테스트 코드 추가/수정                                    |
| **chore**       | 🔧 패키지 매니저 수정 및 기타 잡다한 변경(ex: `.gitignore`) |
| **merge**       | 🔀 브랜치 병합                                              |

💡 **사용 예시:**  
feat: 로그인 폼 생성 완료  
fix: 회원가입 에러 수정

<br>

## 🧠 Branch Strategy

### [Github FLow](https://letzgorats.tistory.com/entry/Git-%ED%98%91%EC%97%85-%ED%94%8C%EB%A1%9C%EC%9A%B0%EB%A5%BC-%EC%82%B4%ED%8E%B4%EB%B3%B4%EC%9E%90)

- ⭐ dev 브랜치는 항상 최신 버전 유지 ⭐
- 개발, 수정사항, 리팩토링 등 모든 작업은 dev 브랜치에서 분기된 브랜치에서 작업
- 이슈 생성하고 브랜치 생성, 개발까지 완료되면 dev 브랜치로 PR
- 리뷰, 테스트 후 main 브랜치로 PR
- ⭐ PR 올리기 전 로컬 dev 브랜치 pull 해서 최신 상태 유지 후, 개발 브랜치로 이동하여 로컬 dev 브랜치 merge 하고 conflict 있다면 해결 후 원격 개발 브랜치로 push ⭐

<br>

## 🌿 Branch Naming 규칙

- 이슈 생성 시 부여된 **Jira Key**를 브랜치명으로 합니다.
- 이슈 생성 시 작업 내용을 상세하게 작성합니다.

💡 **사용 예시:**  
ILKAN-1
