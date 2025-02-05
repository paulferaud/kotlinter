package com.giathuan.kotlinter.ktproto

import com.giathuan.kotlinter.ktproto.support.fix.ExpressionReplacerQuickFix
import com.giathuan.kotlinter.ktproto.support.parser.JavaProtoExpressionResolver.parseJavaGetDefaultInstanceExpression
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.kotlin.idea.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.psi.dotQualifiedExpressionVisitor

/**
 * An IntelliJ inspection to detect Java proto empty creation like `MyMessage.getDefaultInstance()`
 * and suggest transformation to Kotlin DSL like `myMessage {}`.
 */
class KtProtoGetDefaultInstanceInspection : AbstractKotlinInspection() {
  override fun buildVisitor(
      holder: ProblemsHolder,
      isOnTheFly: Boolean,
  ): PsiElementVisitor = dotQualifiedExpressionVisitor { element ->
    val dsl =
        try {
          parseJavaGetDefaultInstanceExpression(element).text()
        } catch (t: Throwable) {
          return@dotQualifiedExpressionVisitor
        }
    holder.registerProblem(
        element.originalElement,
        "Kotlinter: Better DSL for .getDefaultInstance() is available in Kotlin",
        ExpressionReplacerQuickFix(dsl, "Kotlinter: Transform .getDefaultInstance() to Kotlin DS"))
  }
}
