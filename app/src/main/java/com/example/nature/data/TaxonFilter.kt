package com.example.nature.data

import androidx.annotation.StringRes
import com.example.nature.R

enum class TaxonFilter(val iconicTaxonName: String?, @StringRes val label: Int) {
    ALL(null, R.string.filter_all),
    MAMMALIA("Mammalia", R.string.taxon_mammalia),
    AVES("Aves", R.string.taxon_aves),
    INSECTA("Insecta", R.string.taxon_insecta),
    PLANTAE("Plantae", R.string.taxon_plantae),
    FUNGI("Fungi", R.string.taxon_fungi)
}
