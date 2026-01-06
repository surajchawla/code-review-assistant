# 📘 **LLM‑Powered Code Review Assistant**
*A scalable, event‑driven, AI‑augmented code review platform for GitHub pull requests.*

---

## 🚀 Overview

The **LLM‑Powered Code Review Assistant** is a distributed system that automates pull‑request reviews using:

- Static analysis (Checkstyle, PMD, Semgrep)
- Policy‑based rules (YAML‑defined)
- Large Language Models (OpenAI, Azure, Anthropic)
- GitHub inline + summary comments
- Kafka‑based asynchronous orchestration
- Full observability (OpenTelemetry, Prometheus, structured logs)

This platform is designed for **high‑scale engineering organizations** that want consistent, fast, and intelligent code reviews without slowing down developers.

---

## 🧠 Key Features

### 🔗 GitHub Integration
- Secure webhook endpoint
- HMAC SHA‑256 signature validation
- PR metadata extraction
- GitHub App authentication
- Inline + summary comment publishing

### 🧵 Event‑Driven Architecture
- Kafka topics for PR events, review requests, and results
- At‑least‑once processing
- DLQ for failures
- Horizontal scalability

### 🧩 Modular Review Pipeline
- Code fetching & diff processing
- Static analysis engine
- Policy evaluator
- LLM review engine
- Unified findings aggregator

### 📊 Observability
- OpenTelemetry tracing
- Structured JSON logging
- Prometheus metrics
- Grafana dashboards

---

## 🏗️ Architecture

```mermaid
flowchart LR
    subgraph GitHub ["GitHub"]
        A1[Pull Request Event]
        A2[GitHub API (Diff, Files, Comments)]
    end

    subgraph WebhookService ["Webhook Service"]
        B1[Validate HMAC Signature]
        B2[Parse PR Event]
        B3[Publish to Kafka: pr-events]
    end

    subgraph Kafka ["Event Bus (Kafka)"]
        K1[(pr-events)]
        K2[(review-requests)]
        K3[(review-results)]
        KDLQ[(DLQ)]
    end

    subgraph Orchestrator ["Review Orchestrator"]
        O1[Fetch PR Metadata]
        O2[Trigger Code Fetcher]
        O3[Trigger Static Analysis]
        O4[Trigger LLM Review Tasks]
        O5[Aggregate Findings]
        O6[Publish Review Results]
    end

    subgraph CodeFetcher ["Code Fetching & Diff Processing"]
        C1[Fetch Diff / Files]
        C2[Filter Irrelevant Files]
        C3[Chunk Code for LLM]
    end

    subgraph StaticAnalysis ["Static Analysis & Policy Engine"]
        S1[Checkstyle / PMD / Semgrep]
        S2[Policy Evaluator]
        S3[Static Findings]
    end

    subgraph LLMEngine ["LLM Review Engine"]
        L1[Prompt Builder]
        L2[LLM Provider Adapter]
        L3[Parse LLM Output]
        L4[LLM Findings]
    end

    subgraph CommentPublisher ["GitHub Comment Publisher"]
        P1[Inline Comments]
        P2[Summary Comment]
    end

    subgraph Observability ["Observability Layer"]
        OB1[OpenTelemetry Tracing]
        OB2[Structured Logging]
        OB3[Prometheus Metrics]
    end

    A1 --> B1 --> B2 --> B3 --> K1
    K1 --> O1 --> O2 --> C1 --> C2 --> C3 --> K2
    K2 --> O3 --> S1 --> S3 --> O5
    K2 --> O4 --> L1 --> L2 --> L3 --> L4 --> O5
    O5 --> O6 --> K3
    K3 --> CommentPublisher
    CommentPublisher --> A2

    WebhookService --> OB1
    Orchestrator --> OB1
    CodeFetcher --> OB1
    StaticAnalysis --> OB1
    LLMEngine --> OB1
    CommentPublisher --> OB1
```

---

## 🗂️ Repository Structure

```
/code-review-assistant
│
├── webhook-service
│   ├── src/main/java/com/.../webhook
│   ├── src/test/java/com/.../webhook
│   └── README.md
│
├── orchestrator-service
├── code-fetcher-service
├── static-analysis-service
├── llm-review-service
├── comment-publisher-service
│
├── infra
│   ├── helm/
│   ├── terraform/
│   └── docker/
│
├── docs
│   ├── architecture.md
│   ├── sequence-diagrams.md
│   ├── api-contracts.md
│   ├── requirements.md
│   └── epics/
│
└── README.md
```

Each service is independently deployable and testable.

---

## 🔌 Services Overview

### **Webhook Service**
Receives GitHub events, validates signatures, publishes normalized events to Kafka.

### **Review Orchestrator**
Coordinates the entire review workflow.

### **Code Fetcher**
Fetches diffs, filters files, chunks code for LLMs.

### **Static Analysis Service**
Runs Checkstyle, PMD, Semgrep, and policy rules.

### **LLM Review Service**
Builds prompts, calls LLM providers, parses structured output.

### **Comment Publisher**
Posts inline + summary comments back to GitHub.

---

## 🧪 Testing Strategy

### Unit Tests
- Signature validation
- Event parsing
- Prompt building
- Policy evaluation

### Integration Tests
- Kafka Testcontainers
- GitHub API mock
- LLM provider mock

### Load Tests
- Large PRs
- High‑frequency PR events

### Contract Tests
- GitHub API
- LLM provider responses

---

## ☁️ Deployment

### Kubernetes (recommended)
- Each service runs as its own Deployment
- Kafka cluster (MSK, Confluent, or Strimzi)
- OTEL collector
- Prometheus + Grafana

### CI/CD
- GitHub Actions
- Build → Test → Lint → Docker → Deploy

---

## 🔐 Security

- HMAC SHA‑256 signature validation
- GitHub App authentication (JWT)
- Secrets stored in Kubernetes Secret Manager
- Least‑privilege GitHub permissions
- Optional: LLM redaction rules

---

## 📈 Observability

- OpenTelemetry tracing across all services
- Structured JSON logs
- Prometheus metrics
- Grafana dashboards

---

## 🧭 Roadmap

### MVP
- GitHub integration
- Static analysis
- LLM review
- Inline + summary comments
- Basic observability

### Phase 2
- Multi‑LLM routing
- Test generation
- Architectural smell detection
- PR risk scoring

### Phase 3
- Dashboard UI
- Multi‑repo analytics
- Auto‑fix suggestions

---

## 🤝 Contributing

Pull requests are welcome.  
Please follow:

- Conventional commits
- PR templates
- Code style guidelines
- Unit test requirements

---

## 📄 License

MIT (or your preferred license)

