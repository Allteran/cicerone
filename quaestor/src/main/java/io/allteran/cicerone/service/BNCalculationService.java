package io.allteran.cicerone.service;

import io.allteran.cicerone.dto.BNRequest;
import io.allteran.cicerone.dto.BNResponse;
import io.allteran.cicerone.entity.Tax;
import io.allteran.cicerone.entity.calculations.TaxElement;
import io.allteran.cicerone.repo.TaxRepo;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class BNCalculationService {
    private final TaxRepo taxRepo;
    @ConfigProperty(name = "tax.zdw.id")
    String TAX_ZDW_STR;
    private long TAX_ZDW = Long.parseLong(TAX_ZDW_STR);
    @ConfigProperty(name = "tax.pd.id")
    String TAX_D_STR;
    private long TAX_D = Long.parseLong(TAX_D_STR);

    private Uni<List<Tax>> findAllTaxes() {
        return taxRepo.findAll(Sort.ascending("id")).list();
    }

    public BNResponse calculateUop(BNRequest request, List<Tax> taxesFromDb) {
        double netto = request.getBrutto();
        double base = request.getBrutto();
        List<TaxElement> taxes = new ArrayList<>();
        for (int i = 0; i < taxesFromDb.size(); i++) {
            if(taxesFromDb.get(i).getId() == TAX_ZDW) {
                TaxElement zdw = new TaxElement();
                zdw.setName(taxesFromDb.get(i).getName());
                zdw.setTaxId(taxesFromDb.get(i).getId());
                zdw.setPercentage(taxesFromDb.get(i).getPercentage());
                for(int j = 0; j < i - 1; j++) {
                    base = base - taxes.get(j).getResult();
                }
                zdw.setResult(base * zdw.getPercentage());

                taxes.add(zdw);
            } else if(taxesFromDb.get(i).getId() == TAX_D) {
                TaxElement pd = new TaxElement();
                pd.setTaxId(taxesFromDb.get(i).getId());
                pd.setName(taxesFromDb.get(i).getName());
                pd.setPercentage(taxesFromDb.get(i).getPercentage());
                base = base - 250;
                pd.setResult(Math.round(base * pd.getPercentage()));
            } else {
                TaxElement element = new TaxElement();
                element.setTaxId(taxesFromDb.get(i).getId());
                element.setName(taxesFromDb.get(i).getName());
                element.setPercentage(taxesFromDb.get(i).getPercentage());
                element.setResult(base * element.getPercentage());

                taxes.add(element);
            }
        }
        for (TaxElement tm : taxes) {
            netto = netto - tm.getResult();
        }
        if(request.isPit2()) {
            netto = netto - 300;
        }

        return new BNResponse(request.getBrutto(), taxes, netto);
    }

    public Uni<BNResponse> calculateUopTest(BNRequest request) {
        return findAllTaxes().onItem().transform(taxes -> calculateUop(request, taxes));
    }


}
