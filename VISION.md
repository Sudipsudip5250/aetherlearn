# AetherLearn Vision

## Status

This document preserves the long-term north star for **AetherLearn**, the working name for a free and open-source computer-science learning platform. The product name is provisional until a later branding and name-availability check.

## Mission

AetherLearn exists to make rigorous computer and digital-technology education available to anyone, without advertising, surveillance, forced accounts, or dependence on continuous connectivity. It should help a learner move from basic phone and computer literacy to practical programming, systems understanding, responsible security research, and advanced computer-science study.

The product is not intended to be an encyclopedic content dump. Its long-term value should come from a coherent progression, excellent explanations, practical exercises, transparent learning paths, and materials that remain useful when the learner is offline.

## Non-negotiable principles

| Principle | Meaning |
|---|---|
| Free to users | No paywall, freemium gate, hidden charge, advertising, or feature lock in the official distribution. |
| Open source | Application code, content schemas, build tooling, and contribution workflows are published under a permissive license selected at repository creation, with MIT as the default recommendation. |
| Local-first privacy | Progress, notes, bookmarks, quiz scores, and learning preferences remain on the device by default. No account is required for the core experience. |
| Offline continuity | After the initial app and core-content download, the primary learning path works without a network connection. |
| Phone-first practice | A modern Android phone is a complete first-class learning device, not merely a small screen for a desktop product. |
| Termux-aware, not Termux-dependent | Learners may opt into real terminal work, package installation, SSH, and authorized practical labs through Termux or a similar environment, while the core path remains usable without it. |
| Human-readable content | Lessons are understandable to beginners, technically honest at advanced levels, version-aware, and structured for community review. |
| Responsible dual-use education | Sensitive topics are taught through risk tiers, controlled labs, defensive context, explicit authorization boundaries, and safe examples rather than disclaimers alone. |
| Accessible by design | Navigation, reading, interaction, color, motion, and assessment work for learners using different devices and assistive technologies. |

## Long-term product shape

AetherLearn should eventually combine five capabilities: a carefully sequenced curriculum, an offline-capable learning client, browser-safe interactive visualizations, optional real-world terminal practice, and a transparent community contribution system. These capabilities should be built as separable layers so that the project can ship useful releases without waiting for the entire vision to be complete.

The long-term curriculum may extend from foundational digital literacy and the history of computing through programming, algorithms, operating systems, networks, databases, distributed systems, computer architecture, AI, security, scientific computing, quantum computing, graphics, HCI, embedded systems, robotics, and future technologies. The breadth is a roadmap, not a commitment that all topics belong in the first release.

## Phone-plus-Termux differentiator

AetherLearn should make mobile learning practical rather than merely theoretical. A lesson can explain a concept offline, offer a safe simulation inside the app, and then provide an optional bridge to a user-controlled Termux session for real commands, local packages, Git, SSH, or authorized lab work. The bridge must be explicit, inspectable, opt-in, and recoverable when Termux is unavailable.

The product must never imply that a phone is a perfect substitute for every desktop or server workflow. Instead, it should clearly label what can be learned fully offline, what requires an optional local runtime or larger download, and what requires a network, external service, or user-owned system.

## Sustainability direction

The proposed sustainability model is community-funded infrastructure rather than user monetization. Donations, sponsorships, grants, educational partnerships, and volunteer maintenance may support hosting, release signing, accessibility work, and content review. None of these sources may create a paid learning tier or make the core learning path dependent on an account.

If official hosting stops, a learner should still be able to use the latest downloaded application and content, and another community member should be able to mirror releases and content packs from the public repository.

## Success condition

AetherLearn succeeds when a new learner can install it on an Android phone, download a modest core pack once, complete a coherent learning path privately and offline, take notes and quizzes, export their work, and optionally transition from explanation to an authorized local terminal exercise without losing context or being forced into a cloud account.

## References

This vision document is primarily a product decision record based on user-provided requirements. Platform-specific references are maintained in [`ARCHITECTURE.md`](ARCHITECTURE.md).
