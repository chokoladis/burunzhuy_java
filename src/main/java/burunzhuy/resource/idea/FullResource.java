package burunzhuy.resource.idea;

import burunzhuy.entity.Idea;
import burunzhuy.resource.FileResource;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@JsonPropertyOrder({"id", "title", "shortDescription", "preview", "fullDescription", "attaches", "priceMin", "priceInstanceBuy"})
@EqualsAndHashCode
final public class FullResource {
    private final Long id;
    private final String title;
    private final String shortDescription;
    private final FileResource preview;

    private final String fullDescription;
    private final Set<FileResource> attaches;

    private final BigDecimal priceMin;
    private final BigDecimal priceInstanceBuy;

    public FullResource(Idea idea) {
        this.id = idea.getId();
        this.title = idea.getTitle();
        this.shortDescription = idea.getShortDescription();
        this.preview = new FileResource(idea.getPreview());

        this.fullDescription = idea.getFullDescription();
        this.attaches = idea.getAttaches()
                .stream()
                .map(file -> new FileResource(file))
                .collect(Collectors.toSet());

        this.priceMin = idea.getPriceMin();
        this.priceInstanceBuy = idea.getPriceInstanceBuy();
    }
}
