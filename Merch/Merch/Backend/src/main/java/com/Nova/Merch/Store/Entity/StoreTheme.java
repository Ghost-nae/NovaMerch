package com.Nova.Merch.Store.Entity;

import com.Nova.Merch.Common.Model.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "store_themes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_store_theme_store",
                        columnNames = "store_id"
                )
        }
)
public class StoreTheme extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "store_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_store_theme_store"
            )
    )
    @Setter(AccessLevel.NONE)
    private Store store;

    @Column(name = "primary_color", nullable = false, length = 7)
    private String primaryColor = "#111111";

    @Column(name = "secondary_color", nullable = false, length = 7)
    private String secondaryColor = "#FFFFFF";

    @Column(name = "accent_color", nullable = false, length = 7)
    private String accentColor = "#D4AF37";

    @Column(name = "background_color", nullable = false, length = 7)
    private String backgroundColor = "#FFFFFF";

    @Column(name = "text_color", nullable = false, length = 7)
    private String textColor = "#111111";

    @Column(name = "font_family", nullable = false, length = 100)
    private String fontFamily = "Inter";

    @Column(name = "logo_url", length = 2048)
    private String logoUrl;

    @Column(name = "banner_url", length = 2048)
    private String bannerUrl;

    @Column(name = "template", nullable = false, length = 50)
    private String template = "MODERN";

    @Column(name = "button_style", nullable = false, length = 50)
    private String buttonStyle = "ROUNDED";

    public StoreTheme(Store store) {
        this.store = store;
    }
}
