package com.deepflow.app.domain.sentence;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Sentence item shown on the home screen")
public record HomeSentenceResponse(
        @Schema(description = "Sentence content", example = "A room without books is like a body without a soul.")
        String content,
        @Schema(description = "Sentence image URL", example = "https://storage.googleapis.com/deepflow/sentences/example.jpg")
        String imageUrl,
        @Schema(description = "Book title", example = "Selected Essays")
        String bookTitle,
        @Schema(description = "Author name", example = "Marcus Tullius Cicero")
        String author
) {

    public static HomeSentenceResponse from(Sentence sentence) {
        return new HomeSentenceResponse(
                sentence.getContent(),
                sentence.getImageUrl(),
                sentence.getBookTitle(),
                sentence.getAuthor()
        );
    }
}
