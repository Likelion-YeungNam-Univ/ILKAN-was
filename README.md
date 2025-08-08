# ILKAN-was 
> ILKAN 서비스의 Backend Repository입니다.

<br>

## 👨‍💻 Team

| P&D | FE | FE | FE | BE | BE |
|:---:|:--:|:--:|:--:|:--:|:--:|
| <img src="https://avatars.githubusercontent.com/kimsoyun112" height="100"/> <br> [김소윤](https://github.com/kimsoyun112) | <img src="https://avatars.githubusercontent.com/moonchanju" height="100"/> <br> [문찬주](https://github.com/moonchanju) | <img src="https://avatars.githubusercontent.com/cryingdryice" height="100"/> <br> [박지원](https://github.com/cryingdryice) | <img src="https://avatars.githubusercontent.com/nyeonseok" height="100"/> <br> [안현석](https://github.com/nyeonseok) | <img src="https://avatars.githubusercontent.com/KDWorld81" height="100"/> <br> [곽동욱](https://github.com/KDWorld81) | <img src="https://avatars.githubusercontent.com/lnahyun" height="100"/> <br> [이나현](https://github.com/lnahyun) |

<br>

## 👋 Commit Message 규칙

| **메시지 타입** | **설명** |
| --- | --- |
| **feat** | ✨ 새로운 기능 추가 및 기존 기능 수정 |
| **fix** | 🐛 버그 수정 |
| **docs** | 📚 문서 및 주석 수정 |
| **style** | 🎨 코드 스타일 및 포맷팅 수정 |
| **refact** | ♻️ 기능 변화 없는 코드 리팩터링 |
| **test** | ✅ 테스트 코드 추가/수정 |
| **chore** | 🔧 패키지 매니저 수정 및 기타 잡다한 변경(ex: `.gitignore`) |
| **merge** | 🔀 브랜치 병합 |

💡 **사용 예시:**  
feat: 로그인 폼 생성 완료  
fix: 회원가입 에러 수정

<br>

## 🧠 Branch Strategy
### [Github FLow](https://letzgorats.tistory.com/entry/Git-%ED%98%91%EC%97%85-%ED%94%8C%EB%A1%9C%EC%9A%B0%EB%A5%BC-%EC%82%B4%ED%8E%B4%EB%B3%B4%EC%9E%90)
- ⭐ main 브랜치는 항상 최신 버전 유지 ⭐
- 개발, 수정사항, 리팩토링 등 모든 작업은 main 브랜치에서 분기된 브랜치에서 작업
- 이슈 생성하고 브랜치 생성, 개발까지 완료되면 dev 브랜치로 PR
- 리뷰, 테스트 후 main 브랜치로 PR
- 다음 작업도 최신 main 브랜치에서 분기된 브랜치에서 작업
- ⭐ PR 올리기 전 로컬 dev 브랜치 pull 해서 최신 상태 유지 후, 개발 브랜치로 이동하여 로컬 dev 브랜치 merge 하고 conflict 있다면 해결 후 원격 개발 브랜치로 push ⭐

<br>

## 🌿 Branch Naming 규칙

- 이슈 생성 시 부여된 **Jira Key**를 **앞에 붙여서 작성**합니다.  
- 띄어쓰기 대신 언더바를 사용해 가독성을 높입니다.

💡 **사용 예시:**  
ILKAN-1_feat/Search

