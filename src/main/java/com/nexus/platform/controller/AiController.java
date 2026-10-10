package com.nexus.platform.controller;

import com.nexus.platform.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ai")
public class AiController {
    private final ResourceRepository resources;
    private final WebClient client;
    private final boolean enabled;
    private final String apiKey;
    private final String model;

    public AiController(ResourceRepository resources,
                        @Value("${nexus.ai.enabled:false}") boolean enabled,
                        @Value("${nexus.ai.base-url}") String base,
                        @Value("${nexus.ai.api-key:}") String apiKey,
                        @Value("${nexus.ai.model:gpt-4o-mini}") String model) {
        this.resources = resources;
        this.enabled = enabled;
        this.apiKey = apiKey;
        this.model = model;
        this.client = WebClient.builder().baseUrl(base).build();
    }

    @PostMapping("/chat")
    public Map<String, Object> chat(@RequestBody Map<String, String> body) {
        String question = Optional.ofNullable(body.get("message")).orElse("").trim();
        String language = languageName(body.get("language"));
        if (question.isBlank()) return Map.of("answer", "Please enter a question.", "language", language);

        String firstTerm = question.toLowerCase().split("\\s+")[0];
        List<String> context = resources.findAllByOrderByCreatedAtDesc().stream()
                .filter(resource -> (resource.getTitle() + " " + resource.getDescription() + " " + resource.getTags()).toLowerCase().contains(firstTerm))
                .limit(4)
                .map(resource -> resource.getTitle() + ": " + resource.getDescription())
                .toList();

        if (enabled && !apiKey.isBlank()) {
            try {
                Map<String, Object> request = Map.of(
                        "model", model,
                        "messages", List.of(
                                Map.of("role", "system", "content", "You are the NEXUS Assistant for a multi-domain company platform. Answer clearly and concisely in " + language + ". Use the supplied platform resources when relevant. If information is missing, say so and offer a practical next step."),
                                Map.of("role", "user", "content", "Platform resources: " + String.join(" | ", context) + "\nQuestion: " + question)));
                Map<?, ?> response = client.post().uri("/chat/completions")
                        .header("Authorization", "Bearer " + apiKey)
                        .bodyValue(request).retrieve().bodyToMono(Map.class).block();
                Object choices = response == null ? null : response.get("choices");
                if (choices instanceof List<?> list && !list.isEmpty() && list.get(0) instanceof Map<?, ?> choice
                        && choice.get("message") instanceof Map<?, ?> message && message.get("content") != null) {
                    return Map.of("answer", String.valueOf(message.get("content")), "source", context.isEmpty() ? "AI assistant" : "AI + NEXUS resources", "language", language);
                }
            } catch (Exception ignored) {
                // The local reply below keeps the assistant available when the external AI is offline.
            }
        }

        return Map.of("answer", localReply(language, context), "source", "Local NEXUS resources", "language", language);
    }

    private String languageName(String code) {
        if (code == null) return "English";
        return switch (code.toLowerCase()) {
            case "hi" -> "Hindi";
            case "te" -> "Telugu";
            case "ta" -> "Tamil";
            default -> "English";
        };
    }

    private String localReply(String language, List<String> context) {
        if (!context.isEmpty()) {
            String resourcesFound = String.join(" | ", context);
            return switch (language) {
                case "Hindi" -> "NEXUS संसाधनों में यह जानकारी मिली: " + resourcesFound;
                case "Telugu" -> "NEXUS వనరుల్లో ఈ సమాచారం ఉంది: " + resourcesFound;
                case "Tamil" -> "NEXUS வளங்களில் இந்தத் தகவல் உள்ளது: " + resourcesFound;
                default -> "Here is what I found in NEXUS resources: " + resourcesFound;
            };
        }
        return switch (language) {
            case "Hindi" -> "मैं NEXUS सहायक हूँ। इस सवाल के लिए कोई संबंधित संसाधन नहीं मिला। किसी अन्य शब्द से खोजें या व्यवस्थापक से संसाधन साझा करने को कहें। AI जवाबों के लिए व्यवस्थापक AI सेवा चालू कर सकता है।";
            case "Telugu" -> "నేను NEXUS సహాయకుడిని. ఈ ప్రశ్నకు సంబంధిత వనరు దొరకలేదు. మరో పదంతో వెతకండి లేదా నిర్వాహకుడిని వనరును పంచమని అడగండి. AI సమాధానాల కోసం నిర్వాహకుడు AI సేవను ప్రారంభించవచ్చు.";
            case "Tamil" -> "நான் NEXUS உதவியாளர். இந்தக் கேள்விக்கான தொடர்புடைய வளம் கிடைக்கவில்லை. வேறு சொல்லால் தேடுங்கள் அல்லது வளத்தைப் பகிர நிர்வாகியிடம் கேளுங்கள். AI பதில்களுக்கு நிர்வாகி AI சேவையை இயக்கலாம்.";
            default -> "I’m the NEXUS Assistant. I could not find a matching platform resource. Try another keyword or ask an administrator to share a resource. An administrator can enable the AI service for generated answers.";
        };
    }
}
