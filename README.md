# Git 협업 규칙

## 기본 원칙

- 모든 작업은 **브랜치 기반으로 진행**하며 `main`, `develop`에 직접 push 하지 않는다.
- 작업 시작 전 **최신 `develop` 브랜치를 pull** 한다.
- 화면·기능 단위로 작업을 나누고 **PR을 통해 1명 이상 리뷰 후 Squash and merge**로 병합한다.
- 기술적 의견이 다를 경우 **근거 기반으로 논의 후 다수결로 결정**한다.
- 레포는 FE / BE 두 개로 나누고, 아래 규칙은 두 레포에 똑같이 적용한다.

---

## 브랜치 전략

### 브랜치 구조

```txt
main                # 항상 배포 가능한 상태
 └─ develop         # 다음 배포 전 통합 브랜치 (default)
     ├─ feature/*
     ├─ fix/*
     ├─ refactor/*
     └─ chore/*
```

**main**

- 항상 정상 동작해야 하는 배포 브랜치
- 직접 커밋 금지, `develop`에서 PR로만 병합

**develop**

- 각자 작업한 브랜치를 모으는 통합 브랜치 (default 브랜치)
- 주 1회 정도 `main`에 병합해서 배포 상태 갱신

### 작업 브랜치 이름 규칙

```txt
feature/이슈번호-도메인-기능      예: feature/12-room-create
fix/이슈번호-버그내용             예: fix/18-progress-not-updated
refactor/이슈번호-작업내용        예: refactor/21-record-card-split
chore/이슈번호-작업내용           예: chore/3-initial-setup
```

- 도메인은 기능명세서 도메인을 그대로
  쓴다: `auth` `user` `book` `shelf` `log` `calendar` `room` `story` `record` `comment` `home` `common`
- 병합된 브랜치는 삭제한다.

### 작업 순서

1. `develop`에서 새 브랜치 생성 (현재 브랜치: `develop`)
   ```bash
   git checkout develop
   git pull
   git checkout -b feature/12-room-create
   ```
2. 작업 후 커밋 (현재 브랜치: `feature/*`). 브랜치 안의 커밋 메시지는 편하게 적어도 된다 (`wip`, `수정` 등 OK)
   ```bash
   git add .
   git commit -m "wip: 방 만들기 폼"
   git push -u origin feature/12-room-create
   ```
3. GitHub에서 `develop`으로 PR 생성 → 팀원 1명 이상 리뷰
4. 리뷰 반영 후 **Squash and merge**로 병합
    - PR 안의 여러 커밋이 `develop`에는 커밋 1개로 합쳐진다.
    - 병합 시 뜨는 커밋 메시지 입력창(기본값: PR 제목)에 **아래 커밋 컨벤션에 맞는 메시지**를 넣는다.
5. 병합된 브랜치 삭제 후 1번으로 돌아가기

### 병합 주기

- **feature → develop**: 화면·기능 하나 완성될 때마다 수시로 (미루지 않기)
- **develop → main**: 주 1회 (요일 정해두기). 이때 배포까지 같이 진행

### 충돌 방지 팁

- 화면·도메인 단위로 담당을 나눠서 같은 파일을 여러 명이 동시에 건드리지 않게 한다.
- 공용 컴포넌트(`BottomNav`, `ProgressBar`, 책짹이 등)나 공용 엔티티를 수정할 땐 팀 채널에 먼저 알리고 작업한다.
- `develop` pull을 자주 한다.

> **GitHub 설정**
> 레포 Settings → General → Pull Requests에서 **Allow squash merging만 켜고** 나머지 병합 방식은 끈다.
> `main`, `develop`에는 브랜치 보호 규칙(PR 필수, 리뷰 1명 이상)을 건다.

---

## 커밋 규칙

**Squash and merge를 쓰기 때문에, `develop`/`main`에 실제로 남는 커밋 메시지는 PR 제목이다.**

- 작업 브랜치 안의 개별 커밋 메시지는 편하게 적어도 된다. 어차피 병합되면서 하나로 합쳐진다.
- 대신 **PR 제목은 반드시 아래 형식**으로 쓴다. 이게 곧 히스토리가 된다.

```txt
type: 작업 내용 (#이슈번호)
```

예: `feat: 방 만들기 화면 구현 (#12)`

### 타입 목록

| 타입         | 의미                                                | 예시                                 |
|------------|---------------------------------------------------|------------------------------------|
| `feat`     | 새 기능/화면 추가                                        | `feat: 이야기 열기 구현`                  |
| `fix`      | 버그 수정                                             | `fix: 방 기록 작성 시 진도 미반영 수정`         |
| `style`    | 코드 동작에 영향 없는 스타일/포맷 변경 (레이아웃, 색상, 여백 등 UI 스타일 포함) | `style: 책장 반응형 레이아웃 조정`            |
| `refactor` | 기능 변화 없이 코드 구조 개선                                 | `refactor: RecordCard 공용 컴포넌트로 분리` |
| `chore`    | 빌드/설정/패키지 등 기타 작업                                 | `chore: 카카오 로그인 환경변수 설정`           |
| `docs`     | 문서 추가/수정                                          | `docs: README 업데이트`                |
| `test`     | 테스트 코드 추가/수정                                      | `test: 스포일러 블러 판정 로직 테스트 추가`       |
| `perf`     | 성능 개선                                             | `perf: 방 목록 조회 쿼리 최적화`             |
| `remove`   | 파일/코드/기능 삭제                                       | `remove: 사용하지 않는 더미 데이터 삭제`        |

### 작성 팁

- 한 PR에 여러 타입이 섞이면 PR을 나눈다. (예: 기능 추가 + 오타 수정은 따로)
- 내용은 한글로, 무엇을 했는지 명확하게만 적으면 충분하다.
- 이슈 번호가 있으면 뒤에 붙인다: `feat: 방 만들기 화면 구현 (#12)`

---

## Issue 규칙

- 작업 시작 전에 이슈를 먼저 만들고, 이슈 번호로 브랜치를 판다.
- 기능명세서의 기능 하나 ≈ 이슈 하나를 기본으로 하되, 크면 나눈다.
- 이슈 제목: `[Feature] 방 만들기 화면`, `[Bug] 진도가 책장에 반영되지 않음`