package com.pharbers.builder

import com.pharbers.builder.search._

class SearchFacade extends SearchAllMktTrait with SearchHistory
        with SearchSimpleCheckSelect with SearchSimpleCheck with SearchResultCheck with SearchDataExport
