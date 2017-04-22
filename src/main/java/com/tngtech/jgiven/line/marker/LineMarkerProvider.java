package com.tngtech.jgiven.line.marker;

import com.google.common.collect.Iterables;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.impl.LineMarkerNavigator;
import com.intellij.codeInsight.daemon.impl.MarkerType;
import com.intellij.codeInsight.daemon.impl.PsiElementListNavigator;
import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiReference;
import com.tngtech.jgiven.Icons;
import com.tngtech.jgiven.scenario.state.ScenarioStateReferenceProvider;
import com.tngtech.jgiven.util.PsiElementUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LineMarkerProvider implements com.intellij.codeInsight.daemon.LineMarkerProvider {
    private ScenarioStateReferenceProvider scenarioStateReferenceProvider = new ScenarioStateReferenceProvider();

    @Nullable
    @Override
    public MyLineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {
        return collectMarkerFor(element);
    }

    @Nullable
    private MyLineMarkerInfo collectMarkerFor(@NotNull PsiElement element) {
        Optional<PsiField> field = PsiElementUtil.findParentOfTypeOn(element, PsiField.class);
        if (!(element instanceof PsiIdentifier)
                || !(element.getParent() instanceof PsiField)) {
            return null;
        }
        List<PsiField> references = allReferencingFields(field.orElseThrow(IllegalArgumentException::new));
        if (references.isEmpty()) {
            return null;
        }

        return new MyLineMarkerInfo(element, Icons.JGIVEN, new MarkerType("jgiven", (e) -> "JGiven States", navigatorToElements()), "JGiven States");
    }

    @NotNull
    private LineMarkerNavigator navigatorToElements() {
        return new LineMarkerNavigator() {

            @Override
            public void browse(MouseEvent e, PsiElement element) {
                Optional<PsiField> field = PsiElementUtil.findParentOfTypeOn(element, PsiField.class);
                List<PsiField> references = allReferencingFields(field.orElseThrow(IllegalArgumentException::new));
                PsiElementListNavigator.openTargets(e, Iterables.toArray(references, PsiField.class),
                        "JGiven States", "", new DefaultPsiElementCellRenderer());
            }
        };
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {
    }

    private List<PsiField> allReferencingFields(@NotNull PsiField element) {
        return scenarioStateReferenceProvider.findReferences(element, 20)
                .stream().map(this::fieldOf)
                .collect(Collectors.toList());
    }

    private PsiField fieldOf(PsiReference r) {
        return PsiElementUtil.findParentOfTypeOn(r.getElement(), PsiField.class).orElse(null);
    }
}
