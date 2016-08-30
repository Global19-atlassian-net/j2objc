/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.devtools.j2objc.javac;

import com.google.common.collect.Lists;
import com.google.devtools.j2objc.ast.AbstractTypeDeclaration;
import com.google.devtools.j2objc.ast.Annotation;
import com.google.devtools.j2objc.ast.AnnotationTypeDeclaration;
import com.google.devtools.j2objc.ast.AnnotationTypeMemberDeclaration;
import com.google.devtools.j2objc.ast.ArrayAccess;
import com.google.devtools.j2objc.ast.ArrayCreation;
import com.google.devtools.j2objc.ast.ArrayInitializer;
import com.google.devtools.j2objc.ast.ArrayType;
import com.google.devtools.j2objc.ast.AssertStatement;
import com.google.devtools.j2objc.ast.Assignment;
import com.google.devtools.j2objc.ast.Block;
import com.google.devtools.j2objc.ast.BlockComment;
import com.google.devtools.j2objc.ast.BodyDeclaration;
import com.google.devtools.j2objc.ast.BooleanLiteral;
import com.google.devtools.j2objc.ast.BreakStatement;
import com.google.devtools.j2objc.ast.CastExpression;
import com.google.devtools.j2objc.ast.CatchClause;
import com.google.devtools.j2objc.ast.CharacterLiteral;
import com.google.devtools.j2objc.ast.ClassInstanceCreation;
import com.google.devtools.j2objc.ast.Comment;
import com.google.devtools.j2objc.ast.CompilationUnit;
import com.google.devtools.j2objc.ast.ConditionalExpression;
import com.google.devtools.j2objc.ast.ConstructorInvocation;
import com.google.devtools.j2objc.ast.ContinueStatement;
import com.google.devtools.j2objc.ast.CreationReference;
import com.google.devtools.j2objc.ast.DoStatement;
import com.google.devtools.j2objc.ast.EmptyStatement;
import com.google.devtools.j2objc.ast.EnhancedForStatement;
import com.google.devtools.j2objc.ast.EnumConstantDeclaration;
import com.google.devtools.j2objc.ast.EnumDeclaration;
import com.google.devtools.j2objc.ast.Expression;
import com.google.devtools.j2objc.ast.ExpressionMethodReference;
import com.google.devtools.j2objc.ast.ExpressionStatement;
import com.google.devtools.j2objc.ast.FieldAccess;
import com.google.devtools.j2objc.ast.FieldDeclaration;
import com.google.devtools.j2objc.ast.ForStatement;
import com.google.devtools.j2objc.ast.FunctionalExpression;
import com.google.devtools.j2objc.ast.IfStatement;
import com.google.devtools.j2objc.ast.InfixExpression;
import com.google.devtools.j2objc.ast.InstanceofExpression;
import com.google.devtools.j2objc.ast.Javadoc;
import com.google.devtools.j2objc.ast.LabeledStatement;
import com.google.devtools.j2objc.ast.LambdaExpression;
import com.google.devtools.j2objc.ast.LineComment;
import com.google.devtools.j2objc.ast.MarkerAnnotation;
import com.google.devtools.j2objc.ast.MemberValuePair;
import com.google.devtools.j2objc.ast.MethodDeclaration;
import com.google.devtools.j2objc.ast.MethodInvocation;
import com.google.devtools.j2objc.ast.MethodReference;
import com.google.devtools.j2objc.ast.Name;
import com.google.devtools.j2objc.ast.NormalAnnotation;
import com.google.devtools.j2objc.ast.NullLiteral;
import com.google.devtools.j2objc.ast.NumberLiteral;
import com.google.devtools.j2objc.ast.PackageDeclaration;
import com.google.devtools.j2objc.ast.ParameterizedType;
import com.google.devtools.j2objc.ast.ParenthesizedExpression;
import com.google.devtools.j2objc.ast.PostfixExpression;
import com.google.devtools.j2objc.ast.PrefixExpression;
import com.google.devtools.j2objc.ast.PrimitiveType;
import com.google.devtools.j2objc.ast.PropertyAnnotation;
import com.google.devtools.j2objc.ast.QualifiedName;
import com.google.devtools.j2objc.ast.ReturnStatement;
import com.google.devtools.j2objc.ast.SimpleName;
import com.google.devtools.j2objc.ast.SimpleType;
import com.google.devtools.j2objc.ast.SingleMemberAnnotation;
import com.google.devtools.j2objc.ast.SingleVariableDeclaration;
import com.google.devtools.j2objc.ast.SourcePosition;
import com.google.devtools.j2objc.ast.Statement;
import com.google.devtools.j2objc.ast.StringLiteral;
import com.google.devtools.j2objc.ast.SuperConstructorInvocation;
import com.google.devtools.j2objc.ast.SuperFieldAccess;
import com.google.devtools.j2objc.ast.SuperMethodInvocation;
import com.google.devtools.j2objc.ast.SuperMethodReference;
import com.google.devtools.j2objc.ast.SwitchCase;
import com.google.devtools.j2objc.ast.SwitchStatement;
import com.google.devtools.j2objc.ast.SynchronizedStatement;
import com.google.devtools.j2objc.ast.ThisExpression;
import com.google.devtools.j2objc.ast.ThrowStatement;
import com.google.devtools.j2objc.ast.TreeNode;
import com.google.devtools.j2objc.ast.TreeUtil;
import com.google.devtools.j2objc.ast.TryStatement;
import com.google.devtools.j2objc.ast.Type;
import com.google.devtools.j2objc.ast.TypeDeclaration;
import com.google.devtools.j2objc.ast.TypeDeclarationStatement;
import com.google.devtools.j2objc.ast.TypeLiteral;
import com.google.devtools.j2objc.ast.TypeMethodReference;
import com.google.devtools.j2objc.ast.VariableDeclaration;
import com.google.devtools.j2objc.ast.VariableDeclarationExpression;
import com.google.devtools.j2objc.ast.VariableDeclarationFragment;
import com.google.devtools.j2objc.ast.VariableDeclarationStatement;
import com.google.devtools.j2objc.ast.WhileStatement;
import com.google.devtools.j2objc.types.ExecutablePair;
import com.google.devtools.j2objc.util.ElementUtil;
import com.google.devtools.j2objc.util.ErrorUtil;
import com.google.devtools.j2objc.util.FileUtil;
import com.google.devtools.j2objc.util.TranslationEnvironment;
import com.google.devtools.j2objc.util.TypeUtil;
import com.google.j2objc.annotations.Property;
import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree.Kind;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.PackageSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.tree.DocCommentTable;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.JCTree.Tag;
import com.sun.tools.javac.tree.TreeInfo;
import com.sun.tools.javac.util.Position;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;

/**
 * Converts a Java AST from the JDT data structure to our J2ObjC data structure.
 */
public class TreeConverter {
  private final JCTree.JCCompilationUnit unit;
  private CompilationUnit newUnit;

  public static CompilationUnit convertCompilationUnit(
      JavacEnvironment env, JCTree.JCCompilationUnit javacUnit) {
    String sourceFilePath = getPath(javacUnit.getSourceFile());
    try {
      TreeConverter converter = new TreeConverter(javacUnit);
      JavaFileObject sourceFile = javacUnit.getSourceFile();
      String source = sourceFile.getCharContent(false).toString();
      String mainTypeName = FileUtil.getMainTypeName(sourceFile);
      converter.newUnit = new CompilationUnit(new TranslationEnvironment(env), sourceFilePath,
          mainTypeName, source);
      PackageElement pkg = javacUnit.packge != null ? javacUnit.packge : env.defaultPackage();
      SourcePosition pkgPos = converter.getPosition(javacUnit.pid);
      converter.newUnit.setPackage(converter.convertPackage(pkg, pkgPos));
      for (JCTree type : javacUnit.getTypeDecls()) {
        converter.newUnit.addType((AbstractTypeDeclaration) converter.convert(type));
      }
      addOcniComments(converter.newUnit);
      return converter.newUnit;
    } catch (IOException e) {
      ErrorUtil.fatalError(e, sourceFilePath);
      return null;
    }
  }

  private TreeConverter(JCTree.JCCompilationUnit javacUnit) {
    unit = javacUnit;
  }

  private TreeNode convert(Object obj) {
    if (obj == null) {
      return null;
    }
    JCTree node = (JCTree) obj;
    TreeNode newNode = convertInner(node)
        .setPosition(getPosition(node));
    newNode.validate();
    return newNode;
  }

  private SourcePosition getPosition(JCTree node) {
    int startPosition = TreeInfo.getStartPos(node);
    int endPosition = TreeInfo.getEndPos(node, unit.endPositions);
    int length = startPosition == Position.NOPOS || endPosition == Position.NOPOS
        ? 0 : endPosition - startPosition;
    return getSourcePosition(startPosition, length);
  }

  @SuppressWarnings("fallthrough")
  private TreeNode convertInner(JCTree javacNode) {
    switch (javacNode.getKind()) {
      default:
        throw new AssertionError("Unknown node type: " + javacNode.getKind());

      case ANNOTATION:
        return convertAnnotation((JCTree.JCAnnotation) javacNode);
      case ANNOTATION_TYPE:
        return convertAnnotationTypeDeclaration((JCTree.JCClassDecl) javacNode);
      case ARRAY_ACCESS:
        return convertArrayAccess((JCTree.JCArrayAccess) javacNode);
      case ARRAY_TYPE:
        return convertArrayType((JCTree.JCArrayTypeTree) javacNode);
      case ASSERT:
        return convertAssert((JCTree.JCAssert) javacNode);
      case ASSIGNMENT:
        return convertAssignment((JCTree.JCAssign) javacNode);
      case BLOCK:
        return convertBlock((JCTree.JCBlock) javacNode);
      case BREAK:
        return convertBreakStatement((JCTree.JCBreak) javacNode);
      case CASE:
        return convertCase((JCTree.JCCase) javacNode);
      case CATCH:
        return convertCatch((JCTree.JCCatch) javacNode);
      case CLASS:
        return convertClassDeclaration((JCTree.JCClassDecl) javacNode);
      case COMPILATION_UNIT:
        throw new AssertionError(
            "CompilationUnit must be converted using convertCompilationUnit()");
      case CONDITIONAL_EXPRESSION:
        return convertConditionalExpression((JCTree.JCConditional) javacNode);
      case CONTINUE:
        return convertContinueStatement((JCTree.JCContinue) javacNode);
      case DO_WHILE_LOOP:
        return convertDoStatement((JCTree.JCDoWhileLoop) javacNode);
      case EMPTY_STATEMENT:
        return new EmptyStatement();
      case ENHANCED_FOR_LOOP:
        return convertEnhancedForStatement((JCTree.JCEnhancedForLoop) javacNode);
      case ENUM:
        return convertEnum((JCTree.JCClassDecl) javacNode);
      case EXPRESSION_STATEMENT:
        return convertExpressionStatement((JCTree.JCExpressionStatement) javacNode);
      case FOR_LOOP:
        return convertForLoop((JCTree.JCForLoop) javacNode);
      case IDENTIFIER:
        return convertIdent((JCTree.JCIdent) javacNode);
      case INSTANCE_OF:
        return convertInstanceOf((JCTree.JCInstanceOf) javacNode);
      case INTERFACE:
        return convertClassDeclaration((JCTree.JCClassDecl) javacNode);
      case IF:
        return convertIf((JCTree.JCIf) javacNode);
      case LABELED_STATEMENT:
        return convertLabeledStatement((JCTree.JCLabeledStatement) javacNode);
      case LAMBDA_EXPRESSION:
        return convertLambda((JCTree.JCLambda) javacNode);
      case MEMBER_REFERENCE:
        return convertMemberReference((JCTree.JCMemberReference) javacNode);
      case MEMBER_SELECT:
        return convertFieldAccess((JCTree.JCFieldAccess) javacNode);
      case METHOD:
        return convertMethodDeclaration((JCTree.JCMethodDecl) javacNode);
      case METHOD_INVOCATION:
        return convertMethodInvocation((JCTree.JCMethodInvocation) javacNode);
      case NEW_ARRAY:
        return convertNewArray((JCTree.JCNewArray) javacNode);
      case NEW_CLASS:
        return convertNewClass((JCTree.JCNewClass) javacNode);
      case PARAMETERIZED_TYPE:
        return convertTypeApply((JCTree.JCTypeApply) javacNode);
      case PARENTHESIZED:
        return convertParens((JCTree.JCParens) javacNode);
      case PRIMITIVE_TYPE:
        return convertPrimitiveType((JCTree.JCPrimitiveTypeTree) javacNode);
      case RETURN:
        return convertReturn((JCTree.JCReturn) javacNode);
      case SWITCH:
        return convertSwitch((JCTree.JCSwitch) javacNode);
      case THROW:
        return convertThrow((JCTree.JCThrow) javacNode);
      case TRY:
        return convertTry((JCTree.JCTry) javacNode);
      case TYPE_CAST:
        return convertTypeCast((JCTree.JCTypeCast) javacNode);
      case VARIABLE:
        return convertVariableDeclaration((JCTree.JCVariableDecl) javacNode);
      case WHILE_LOOP:
        return convertWhileLoop((JCTree.JCWhileLoop) javacNode);

      case BOOLEAN_LITERAL:
        return convertBooleanLiteral((JCTree.JCLiteral) javacNode);
      case CHAR_LITERAL:
        return convertCharLiteral((JCTree.JCLiteral) javacNode);
      case DOUBLE_LITERAL:
      case FLOAT_LITERAL:
      case INT_LITERAL:
      case LONG_LITERAL:
        return convertNumberLiteral((JCTree.JCLiteral) javacNode);
      case STRING_LITERAL:
        return convertStringLiteral((JCTree.JCLiteral) javacNode);
      case SYNCHRONIZED:
        return convertSynchronized((JCTree.JCSynchronized) javacNode);
      case NULL_LITERAL:
        return new NullLiteral(((JCTree.JCLiteral) javacNode).type);

      case AND:
      case CONDITIONAL_AND:
      case CONDITIONAL_OR:
      case DIVIDE:
      case EQUAL_TO:
      case GREATER_THAN:
      case GREATER_THAN_EQUAL:
      case LEFT_SHIFT:
      case LESS_THAN:
      case LESS_THAN_EQUAL:
      case MINUS:
      case MULTIPLY:
      case NOT_EQUAL_TO:
      case OR:
      case PLUS:
      case REMAINDER:
      case RIGHT_SHIFT:
      case UNSIGNED_RIGHT_SHIFT:
      case XOR:
        return convertBinary((JCTree.JCBinary) javacNode);

      case BITWISE_COMPLEMENT:
      case LOGICAL_COMPLEMENT:
      case PREFIX_DECREMENT:
      case PREFIX_INCREMENT:
      case UNARY_MINUS:
      case UNARY_PLUS:
        return convertPrefixExpr((JCTree.JCUnary) javacNode);

      case POSTFIX_DECREMENT:
      case POSTFIX_INCREMENT:
        return convertPostExpr((JCTree.JCUnary) javacNode);

      case AND_ASSIGNMENT:
      case DIVIDE_ASSIGNMENT:
      case LEFT_SHIFT_ASSIGNMENT:
      case MINUS_ASSIGNMENT:
      case MULTIPLY_ASSIGNMENT:
      case OR_ASSIGNMENT:
      case PLUS_ASSIGNMENT:
      case REMAINDER_ASSIGNMENT:
      case RIGHT_SHIFT_ASSIGNMENT:
      case UNSIGNED_RIGHT_SHIFT_ASSIGNMENT:
      case XOR_ASSIGNMENT:
        return convertAssignOp((JCTree.JCAssignOp) javacNode);

      case OTHER: {
        if (javacNode.hasTag(Tag.NULLCHK)) {
          // Skip javac's nullchk operators, since j2objc provides its own.
          // TODO(tball): convert to nil_chk() functions in this class, to
          // always check references that javac flagged?
          return convert(((JCTree.JCUnary) javacNode).arg);
        }
        throw new AssertionError("Unknown OTHER node, tag: " + javacNode.getTag());
      }
    }
  }

  private TreeNode convertAbstractTypeDeclaration(
      JCTree.JCClassDecl node, AbstractTypeDeclaration newNode) {
    convertBodyDeclaration(node, newNode);
    List<BodyDeclaration> bodyDeclarations = new ArrayList<>();
    for (JCTree bodyDecl : node.getMembers()) {
      // Skip synthetic methods. Synthetic default constructors are not marked
      // synthetic for backwards-compatibility reasons, so they are detected
      // by a source position that's the same as their declaring class.
      // TODO(tball): keep synthetic default constructors after front-end switch,
      // and get rid of DefaultConstructorAdder translator.
      if (bodyDecl.getKind() == Kind.METHOD
          && (ElementUtil.isSynthetic(((JCTree.JCMethodDecl) bodyDecl).sym)
              || bodyDecl.pos == node.pos)) {
        continue;
      }
      Object member = convert(bodyDecl);
      if (member instanceof BodyDeclaration) {  // Not true for enum constants.
        bodyDeclarations.add((BodyDeclaration) member);
      }
    }
    return newNode
        .setName(convertSimpleName(node.sym, node.sym.asType(), getNamePosition(node)))
        .setTypeElement(node.sym)
        .setBodyDeclarations(bodyDeclarations);
  }

  private TreeNode convertAnnotation(JCTree.JCAnnotation node) {
    List<JCTree.JCExpression> args = node.getArguments();
    Annotation newNode;
    if (args.isEmpty()) {
      newNode = new MarkerAnnotation()
          .setAnnotationMirror(node.attribute);
    } else if (args.size() == 1) {
      String annotationName = node.getAnnotationType().toString();
      if (annotationName.equals(Property.class.getSimpleName())
          || annotationName.equals(Property.class.getName())) {
        newNode = new PropertyAnnotation();
        //TODO(tball): parse property attribute string.
        throw new AssertionError("not implemented");
      } else {
        newNode = new SingleMemberAnnotation()
            .setValue((Expression) convert(args.get(0)));
      }
    } else {
      NormalAnnotation normalAnn = new NormalAnnotation();
      for (JCTree.JCExpression obj : node.getArguments()) {
        JCTree.JCAssign assign = (JCTree.JCAssign) obj;
        Symbol sym = ((JCTree.JCIdent) assign.lhs).sym;
        MemberValuePair memberPair = new MemberValuePair()
            .setName(convertSimpleName(sym, sym.asType(), getPosition(assign.lhs)))
            .setValue((Expression) convert(assign.rhs));
        normalAnn.addValue(memberPair);
      }
      newNode = normalAnn;
    }
    return newNode
        .setAnnotationMirror(node.attribute)
        .setTypeName((Name) convert(node.getAnnotationType()));
  }

  private TreeNode convertAnnotationTypeDeclaration(JCTree.JCClassDecl node) {
    AnnotationTypeDeclaration newNode = new AnnotationTypeDeclaration();
    convertBodyDeclaration(node, newNode);
    for (JCTree bodyDecl : node.getMembers()) {
      if (bodyDecl.getKind() == Kind.METHOD) {
        JCTree.JCMethodDecl methodDecl = (JCTree.JCMethodDecl) bodyDecl;
        AnnotationTypeMemberDeclaration newMember = new AnnotationTypeMemberDeclaration()
            .setName(
                convertSimpleName(methodDecl.sym, methodDecl.type, getNamePosition(methodDecl)))
            .setDefault((Expression) convert(methodDecl.defaultValue))
            .setExecutableElement(methodDecl.sym)
            .setType(Type.newType(methodDecl.sym.getReturnType()));
        List<Annotation> annotations = new ArrayList<>();
        for (AnnotationTree annotation : methodDecl.mods.annotations) {
          annotations.add((Annotation) convert(annotation));
        }
        newMember
            .setModifiers((int) methodDecl.getModifiers().flags)
            .setAnnotations(annotations)
            .setJavadoc((Javadoc) getAssociatedJavaDoc(methodDecl));
        newNode.addBodyDeclaration(newMember);
      } else {
        newNode.addBodyDeclaration((BodyDeclaration) convert(bodyDecl));
      }
    }
    return newNode
        .setName(convertSimpleName(node.sym, node.type, getNamePosition(node)))
        .setTypeElement(node.sym);
  }

  private TreeNode convertArrayAccess(JCTree.JCArrayAccess node) {
    return new ArrayAccess()
        .setArray((Expression) convert(node.getExpression()))
        .setIndex((Expression) convert(node.getIndex()));
  }

  private TreeNode convertArrayType(JCTree.JCArrayTypeTree node) {
    ArrayType newNode = new ArrayType();
    convertType(node, newNode);
    Type componentType = (Type) Type.newType(node.getType().type);
    return newNode.setComponentType(componentType);
  }

  private TreeNode convertAssert(JCTree.JCAssert node) {
    return new AssertStatement()
        .setExpression((Expression) convert(node.getCondition()))
        .setMessage((Expression) convert(node.getDetail()));
  }

  private TreeNode convertAssignment(JCTree.JCAssign node) {
    Assignment newNode = new Assignment();
    convertExpression(node, newNode);
    return newNode
        .setOperator(Assignment.Operator.ASSIGN)
        .setLeftHandSide((Expression) convert(node.getVariable()))
        .setRightHandSide((Expression) convert(node.getExpression()));
  }

  private TreeNode convertAssignOp(JCTree.JCAssignOp node) {
    Assignment newNode = new Assignment();
    convertExpression(node, newNode);
    String operatorName = node.getOperator().getSimpleName().toString() + "=";
    return newNode
        .setOperator(Assignment.Operator.fromJdtOperatorName(operatorName))
        .setLeftHandSide((Expression) convert(node.getVariable()))
        .setRightHandSide((Expression) convert(node.getExpression()));
  }

  private TreeNode convertBinary(JCTree.JCBinary node) {
    InfixExpression newNode = new InfixExpression();
    convertExpression(node, newNode);
    newNode
        .setTypeMirror(node.type)
        .setOperator(InfixExpression.Operator.parse(node.operator.name.toString()));

    // Flatten this tree to avoid stack overflow with very deep trees. This
    // code traverses the subtree non-recursively and merges all children
    // that have the same operator into this node.
    List<StackState> stack = Lists.newArrayList();
    stack.add(new StackState(node));
    while (!stack.isEmpty()) {
      StackState currentState = stack.get(stack.size() - 1);
      JCTree.JCExpression child = currentState.nextChild();
      if (child == null) {
        stack.remove(stack.size() - 1);
        continue;
      }
      if (child instanceof JCTree.JCBinary) {
        JCTree.JCBinary infixChild = (JCTree.JCBinary) child;
        if (infixChild.getKind() == node.getKind()) {
          stack.add(new StackState(infixChild));
          continue;
        }
      }
      newNode.addOperand((Expression) convert(child));
    }
    return newNode;
  }



  private TreeNode convertBlock(JCTree.JCBlock node) {
    Block newNode = new Block();
    for (StatementTree stmt : node.getStatements()) {
      TreeNode tree = convert(stmt);
      if (tree instanceof AbstractTypeDeclaration) {
        tree = new TypeDeclarationStatement().setDeclaration((AbstractTypeDeclaration) tree);
      }
      newNode.addStatement((Statement) tree);
    }
    return newNode;
  }

  private TreeNode convertBodyDeclaration(JCTree.JCClassDecl node, BodyDeclaration newNode) {
    List<Annotation> annotations = new ArrayList<>();
    for (AnnotationTree annotation : node.getModifiers().getAnnotations()) {
      annotations.add((Annotation) convert(annotation));
    }
    return newNode
        .setModifiers((int) node.getModifiers().flags)
        .setAnnotations(annotations)
        .setJavadoc((Javadoc) getAssociatedJavaDoc(node));
  }

  private TreeNode convertBooleanLiteral(JCTree.JCLiteral node) {
    return new BooleanLiteral((Boolean) node.getValue(), node.type);
  }

  private TreeNode convertBreakStatement(JCTree.JCBreak node) {
    BreakStatement newNode = new BreakStatement();
    Object label = node.getLabel();
    if (label != null) {
      newNode.setLabel((SimpleName)
          new SimpleName(label.toString()).setPosition(getPosition(node)));
    }
    return newNode;
  }

  private TreeNode convertCase(JCTree.JCCase node) {
    // Case statements are converted in convertSwitch().
    SwitchCase newNode = new SwitchCase();
    if (node.pat != null) {
      newNode.setExpression((Expression) convert(node.getExpression()));
    } else {
      newNode.setIsDefault(true);
    }
    return newNode;
  }

  private TreeNode convertCatch(JCTree.JCCatch node) {
    return new CatchClause()
        .setException((SingleVariableDeclaration) convert(node.getParameter()))
        .setBody((Block) convert(node.getBlock()));
  }

  private TreeNode convertCharLiteral(JCTree.JCLiteral node) {
    return new CharacterLiteral((Character) node.getValue(), node.type);
  }

  private TreeNode convertClassDeclaration(JCTree.JCClassDecl node) {
    // javac defines all type declarations with JCClassDecl, so differentiate here
    // to support our different declaration nodes.
    if (node.sym.isAnonymous()) {
      // TODO(kstanger): See if anonymous classes can follow the same code branch as regular class
      // declarations.
      TypeDeclaration newNode = new TypeDeclaration(node.sym);
      for (JCTree bodyDecl : node.getMembers()) {
        Object member = convert(bodyDecl);
        if (member instanceof BodyDeclaration) {  // Not true for enum constants.
          newNode.addBodyDeclaration((BodyDeclaration) member);
        }
      }
      return newNode;
    }
    if (node.sym.getKind() == ElementKind.ANNOTATION_TYPE) {
      throw new AssertionError("Annotation type declaration tree conversion not implemented");
    }

    TypeDeclaration newNode =
        (TypeDeclaration) convertAbstractTypeDeclaration(node, new TypeDeclaration());

    JCTree.JCExpression extendsClause = node.getExtendsClause();
    if (extendsClause != null) {
      newNode.setSuperclassType(Type.newType(nameType((node.getExtendsClause()))));
    }
    newNode.setInterface(
        node.getKind() == Kind.INTERFACE || node.getKind() == Kind.ANNOTATION_TYPE);
    for (JCTree superInterface : node.getImplementsClause()) {
      newNode.addSuperInterfaceType(Type.newType(nameType(superInterface)));
    }
    return newNode;
  }

  private TreeNode convertConditionalExpression(JCTree.JCConditional node) {
    return new ConditionalExpression()
        .setTypeMirror(node.type)
        .setExpression((Expression) convert(node.getCondition()))
        .setThenExpression((Expression) convert(node.getTrueExpression()))
        .setElseExpression((Expression) convert(node.getFalseExpression()));
  }

  private TreeNode convertContinueStatement(JCTree.JCContinue node) {
    ContinueStatement newNode = new ContinueStatement();
    Object label = node.getLabel();
    if (label != null) {
        newNode.setLabel((SimpleName)
            new SimpleName(label.toString()).setPosition(getPosition(node)));
    }
    return newNode;
  }

  private TreeNode convertDoStatement(JCTree.JCDoWhileLoop node) {
    return new DoStatement()
        .setExpression(convertWithoutParens(node.getCondition()))
        .setBody((Statement) convert(node.getStatement()));
  }

  private TreeNode convertEnhancedForStatement(JCTree.JCEnhancedForLoop node) {
    return new EnhancedForStatement()
        .setParameter((SingleVariableDeclaration) convertSingleVariable(node.getVariable()))
        .setExpression((Expression) convert(node.getExpression()))
        .setBody((Statement) convert(node.getStatement()));
  }

  private TreeNode convertEnum(JCTree.JCClassDecl node) {
    if (node.sym.isAnonymous()) {
      return (TypeDeclaration) convertClassDeclaration(node);
    }
    EnumDeclaration newNode = (EnumDeclaration) new EnumDeclaration()
        .setName(convertSimpleName(node.sym, node.type, getNamePosition(node)))
        .setTypeElement(node.sym);
    for (JCTree superInterface : node.getImplementsClause()) {
      newNode.addSuperInterfaceType(Type.newType(nameType(superInterface)));
    }
    for (JCTree bodyDecl : node.getMembers()) {
      if (bodyDecl.getKind() == Kind.VARIABLE) {
        TreeNode var = convertVariableDeclaration((JCTree.JCVariableDecl) bodyDecl);
        if (var.getKind() == TreeNode.Kind.ENUM_CONSTANT_DECLARATION) {
          newNode.addEnumConstant((EnumConstantDeclaration) var);
        } else {
          newNode.addBodyDeclaration((BodyDeclaration) var);
        }
      } else if (bodyDecl.getKind() == Kind.METHOD) {
        MethodDeclaration method = (MethodDeclaration)
            convertMethodDeclaration((JCTree.JCMethodDecl) bodyDecl);
        if (ElementUtil.isConstructor(method.getExecutableElement())
            && !method.getBody().getStatements().isEmpty()){
          // Remove bogus "super()" call from constructors, so InitializationNormalizer
          // adds the correct super call that includes the ordinal and name arguments.
          Statement stmt = method.getBody().getStatements().get(0);
          if (stmt.getKind() == TreeNode.Kind.SUPER_CONSTRUCTOR_INVOCATION) {
            SuperConstructorInvocation call = (SuperConstructorInvocation) stmt;
            if (call.getArguments().isEmpty()) {
              method.getBody().getStatements().remove(0);
            }
          }
        }
        newNode.addBodyDeclaration(method);
      } else {
        newNode.addBodyDeclaration((BodyDeclaration) convert(bodyDecl));
      }
    }
    return newNode;
  }

  private TreeNode convertExpression(
      JCTree.JCExpression node, Expression newNode) {
    Object value = node.type.constValue();
    // Convert boolean values of 1/0 as true/false.
    if (TypeUtil.isBoolean(node.type.baseType()) && value instanceof Integer) {
      value = ((Integer) value).intValue() == 1;
    }
    return newNode
        .setConstantValue(value);
  }

  private TreeNode convertExpressionStatement(JCTree.JCExpressionStatement node) {
    TreeNode expr = convert(node.getExpression());
    if (expr instanceof Statement) {
      return expr;
    }
    return new ExpressionStatement().setExpression((Expression) expr);
  }

  private TreeNode convertFieldAccess(JCTree.JCFieldAccess node) {
    String fieldName = node.name.toString();
    SourcePosition pos = getPosition(node);
    JCTree.JCExpression selected = node.getExpression();
    if (fieldName.equals("this")) {
      return new ThisExpression()
          .setQualifier((Name) convert(selected))
          .setTypeMirror(node.sym.asType());
    }
    if (selected.toString().equals("super")) {
      return new SuperFieldAccess()
          .setVariableElement((VariableElement) node.sym)
          .setName(convertSimpleName(node.sym, node.type, pos));
    }
    if (node.getIdentifier().toString().equals("class")) {
      return new TypeLiteral(node.type)
          .setType((Type) convertType(selected.type, pos, false));
    }
    if (selected.getKind() == Kind.IDENTIFIER && !node.sym.getKind().isField()) {
      JCIdent ident = (JCTree.JCIdent) selected;
      return new QualifiedName()
          .setName(convertSimpleName(node.sym, node.type, pos))
          .setQualifier(convertSimpleName(ident.sym, ident.type, pos))
          .setElement(node.sym);
    }
    if (selected.getKind() == Kind.MEMBER_SELECT) {
      TreeNode newSelected = convertFieldAccess((JCTree.JCFieldAccess) selected).setPosition(pos);
      if (newSelected.getKind() == TreeNode.Kind.QUALIFIED_NAME) {
        return new QualifiedName()
            .setName(convertSimpleName(node.sym, node.type, pos))
            .setQualifier((QualifiedName) newSelected)
            .setElement(node.sym);
      }
    }
    return new FieldAccess()
        .setVariableElement((VariableElement) node.sym)
        .setExpression((Expression) convert(selected))
        .setName(convertSimpleName(node.sym, node.type, pos).setTypeMirror(node.type));
  }

  private TreeNode convertForLoop(JCTree.JCForLoop node) {
    ForStatement newNode = new ForStatement()
        .setExpression((Expression) convert(node.getCondition()))
        .setBody((Statement) convert(node.getStatement()));
    for (JCTree.JCStatement initializer : node.getInitializer()) {
      if (initializer.getKind() == Kind.VARIABLE) {
        JCTree.JCVariableDecl var = (JCTree.JCVariableDecl) initializer;
        newNode.addInitializer((Expression) convertVariableExpression(var));
      } else {
        assert initializer.getKind() == Kind.EXPRESSION_STATEMENT;
        newNode.addInitializer((Expression)
          convert(((JCTree.JCExpressionStatement) initializer).getExpression()));
      }
    }
    for (JCTree.JCExpressionStatement updater : node.getUpdate()) {
      newNode.addUpdater((Expression) convert(updater.getExpression()));
    }
    return newNode;
  }

  private TreeNode convertFunctionalExpression(JCTree.JCFunctionalExpression node,
      FunctionalExpression newNode) {
    convertExpression(node, newNode);
    for (TypeMirror type : node.targets) {
      newNode.addTargetType(type);
    }
    return newNode.setTypeMirror(node.type);
  }

  private TreeNode convertIdent(JCTree.JCIdent node) {
    String text = node.sym.toString();
    if (text.equals("this")) {
      return new ThisExpression().setTypeMirror(node.type);
    }
    SimpleName newNode = new SimpleName(node.sym, node.type);
    convertExpression(node, newNode);
    return newNode;
  }

  private TreeNode convertIf(JCTree.JCIf node) {
    return new IfStatement()
        .setExpression(convertWithoutParens(node.getCondition()))
        .setThenStatement((Statement) convert(node.getThenStatement()))
        .setElseStatement((Statement) convert(node.getElseStatement()));
  }

  private TreeNode convertInstanceOf(JCTree.JCInstanceOf node) {
    TypeMirror clazz = nameType(node.getType());
    return new InstanceofExpression()
        .setLeftOperand((Expression) convert(node.getExpression()))
        .setRightOperand(Type.newType(clazz))
        .setTypeMirror(node.getType().type);
  }

  private TreeNode convertLabeledStatement(JCTree.JCLabeledStatement node) {
    return new LabeledStatement()
        .setLabel((SimpleName) new SimpleName(node.label.toString()).setPosition(getPosition(node)))
        .setBody((Statement) convert(node.body));
  }

  private TreeNode convertLambda(JCTree.JCLambda node) {
    LambdaExpression newNode = new LambdaExpression();
    convertFunctionalExpression(node, newNode);
    for (JCVariableDecl param : node.params) {
      newNode.addParameter((VariableDeclaration) convert(param));
    }
    return newNode.setBody(convert(node.getBody()));
  }

  private TreeNode convertMethodReference(JCTree.JCMemberReference node, MethodReference newNode) {
    convertFunctionalExpression(node, newNode);
    if (node.getTypeArguments() != null) {
      for (Object typeArg : node.getTypeArguments()) {
        newNode.addTypeArgument((Type) convert(typeArg));
      }
    }
    return newNode
        .setExecutablePair(new ExecutablePair((ExecutableElement) node.sym));
  }

  private TreeNode convertMemberReference(JCTree.JCMemberReference node) {
    Element element = node.sym;
    SourcePosition pos = getPosition(node);
    if (ElementUtil.isConstructor(element)) {
      CreationReference newNode = new CreationReference();
      convertMethodReference(node, newNode);
      return newNode
          .setType(Type.newType(nameType(node.expr)));
    }
    if (node.hasKind(JCTree.JCMemberReference.ReferenceKind.SUPER)) {
      SuperMethodReference newNode = new SuperMethodReference();
      convertMethodReference(node, newNode);
      // Qualifier expression is <name>."super", so it's always a JCFieldAccess.
      JCTree.JCFieldAccess expr = (JCTree.JCFieldAccess) node.getQualifierExpression();
      return newNode
          .setName(convertSimpleName(node.sym, node.type, getPosition(expr)))
          .setQualifier(
              convertSimpleName(nameSymbol(expr.selected), expr.type, getPosition(expr.selected)));
    }
    if (node.hasKind(JCTree.JCMemberReference.ReferenceKind.UNBOUND)
        || node.hasKind(JCTree.JCMemberReference.ReferenceKind.STATIC)) {
      TypeMethodReference newNode = new TypeMethodReference();
      convertMethodReference(node, newNode);
      return newNode
          .setName(convertSimpleName(node.sym, node.type, pos))
          .setType(convertType(node.type, pos, false));
    }

    ExpressionMethodReference newNode = new ExpressionMethodReference();
    convertMethodReference(node, newNode);
    return newNode
        .setName(convertSimpleName(node.sym, node.type, pos))
        .setExpression((Expression) convert(node.getQualifierExpression()));
  }

  private TreeNode convertMethodDeclaration(JCTree.JCMethodDecl node) {
    MethodDeclaration newNode = new MethodDeclaration();
    List<Annotation> annotations = new ArrayList<>();
    for (AnnotationTree annotation : node.getModifiers().getAnnotations()) {
      annotations.add((Annotation) convert(annotation));
    }
    for (JCTree.JCVariableDecl param : node.getParameters()) {
      newNode.addParameter((SingleVariableDeclaration) convert(param));
    }
    if (ElementUtil.isConstructor(node.sym)) {
      TypeElement declaringClass = ElementUtil.getDeclaringClass(node.sym);
      int classNameLength = declaringClass.getSimpleName().toString().length();
      SourcePosition pos = getSourcePosition(node.pos, classNameLength);
      SimpleName constructorName = convertSimpleName(declaringClass, declaringClass.asType(), pos);
      newNode
          .setName(constructorName)
          .setIsConstructor(true);
    } else {
      newNode
          .setName(convertSimpleName(node.sym, node.type, getNamePosition(node)))
          .setReturnType(Type.newType(node.type.asMethodType().getReturnType()));
    }
    return newNode
        .setExecutableElement(node.sym)
        .setBody((Block) convert(node.getBody()))
        .setModifiers((int) node.getModifiers().flags)
        .setAnnotations(annotations)
        .setJavadoc((Javadoc) getAssociatedJavaDoc(node));
  }

  private TreeNode convertMethodInvocation(JCTree.JCMethodInvocation node) {
    JCTree.JCExpression method = node.getMethodSelect();
    if (method.getKind() == Kind.IDENTIFIER) {
      ExecutableElement element = (ExecutableElement) ((JCTree.JCIdent) method).sym;
      if (method.toString().equals("this")) {
        ConstructorInvocation newNode = new ConstructorInvocation()
            .setExecutablePair(new ExecutablePair(element));
        for (JCTree.JCExpression arg : node.getArguments()) {
          newNode.addArgument((Expression) convert(arg));
        }
        return newNode;
      }
      if (method.toString().equals("super")) {
        SuperConstructorInvocation newNode = new SuperConstructorInvocation()
            .setExecutablePair(new ExecutablePair(element));
        for (JCTree.JCExpression arg : node.getArguments()) {
          newNode.addArgument((Expression) convert(arg));
        }
        // If there's no expression node, javac sets it to be "<init>" which we don't want.
        Expression expr = ((Expression) convert(method));
        if (!expr.toString().equals("<init>")) {
          newNode.setExpression(expr);
        }
        return newNode;
      }

    }
    if (method.getKind() == Kind.MEMBER_SELECT
        && ((JCTree.JCFieldAccess) method).selected.toString().equals("super")) {
      JCTree.JCFieldAccess select = (JCTree.JCFieldAccess) method;
      Symbol.MethodSymbol sym = (Symbol.MethodSymbol) ((JCTree.JCFieldAccess) method).sym;
      SuperMethodInvocation newNode = new SuperMethodInvocation()
          .setExecutablePair(new ExecutablePair(sym, (ExecutableType) select.type))
          .setName(convertSimpleName(sym, method.type, getPosition(node)));
      if (select.selected.getKind() == Kind.MEMBER_SELECT) {
        // foo.bar.MyClass.super.print(...):
        //   select: foo.bar.MyClass.super.print
        //   select.selected: foo.bar.MyClass.super
        //   select.selected.selected: foo.bar.MyClass
        newNode.setQualifier((Name) convert(((JCTree.JCFieldAccess) select.selected).selected));
       }
      for (JCTree.JCExpression arg : node.getArguments()) {
        newNode.addArgument((Expression) convert(arg));
      }
      return newNode;
    }

    MethodInvocation newNode = new MethodInvocation();
    ExecutableElement sym;
    ExecutableType type;
    if (method.getKind() == Kind.IDENTIFIER) {
      sym = (ExecutableElement) ((JCTree.JCIdent) method).sym;
      type = (ExecutableType) method.type;
      newNode.setName((SimpleName) convert(method));
    } else {
      JCTree.JCFieldAccess select = (JCTree.JCFieldAccess) method;
      sym = (ExecutableElement) select.sym;
      type = (ExecutableType) select.type;
      newNode
          .setName(convertSimpleName(select.sym, select.type, getPosition(select)))
          .setExpression((Expression) convert(select.selected));
    }
    for (JCTree.JCExpression arg : node.getArguments()) {
      newNode.addArgument((Expression) convert(arg));
    }
    return newNode
        .setTypeMirror(node.type)
        .setExecutablePair(new ExecutablePair(sym, type));
  }

  private SimpleName convertSimpleName(Element element, TypeMirror type, SourcePosition pos) {
    return (SimpleName) new SimpleName(element, type).setPosition(pos);
  }

  private Name convertName(Symbol symbol, SourcePosition pos) {
    if (symbol.owner == null || symbol.owner.name.isEmpty()) {
      return new SimpleName(symbol);
    }
    return new QualifiedName(symbol, symbol.asType(), convertName(symbol.owner, pos));
  }

  private TreeNode convertNewArray(JCTree.JCNewArray node) {
    ArrayCreation newNode = new ArrayCreation();
    convertExpression(node, newNode);
    List<Expression> dimensions = new ArrayList<>();
    for (JCTree.JCExpression dimension : node.getDimensions()) {
      dimensions.add((Expression) convert(dimension));
    }
    javax.lang.model.type.ArrayType type = (javax.lang.model.type.ArrayType) node.type;
    if (node.getInitializers() != null) {
      ArrayInitializer initializers = new ArrayInitializer(type);
      for (JCTree.JCExpression initializer : node.getInitializers()) {
        initializers.addExpression((Expression) convert(initializer));
      }
      newNode.setInitializer(initializers);
    }
    return newNode
        .setType((ArrayType) new ArrayType(type).setPosition(getPosition(node)))
        .setDimensions(dimensions);
  }

  private TreeNode convertNewClass(JCTree.JCNewClass node) {
    ClassInstanceCreation newNode = new ClassInstanceCreation();
    convertExpression(node, newNode);
    for (JCTree.JCExpression arg : node.getArguments()) {
      newNode.addArgument((Expression) convert(arg));
    }
    return newNode
        .setExecutablePair(new ExecutablePair((ExecutableElement) node.constructor))
        .setExpression((Expression) convert(node.getEnclosingExpression()))
        .setType(Type.newType(node.type))
        .setAnonymousClassDeclaration((TypeDeclaration) convert(node.def));
  }

  private TreeNode convertNumberLiteral(JCTree.JCLiteral node) {
    return new NumberLiteral((Number) node.getValue(), node.type)
        .setToken(getTreeSource(node));
  }

  private PackageDeclaration convertPackage(PackageElement pkg, SourcePosition namePos) {
    // javac doesn't include the "package" token in its AST, just the package name.
    PackageDeclaration newNode = new PackageDeclaration()
        .setPackageElement(pkg);
    return (PackageDeclaration) newNode.setName(convertName((PackageSymbol) pkg, namePos))
        .setPosition(SourcePosition.NO_POSITION);
  }

  private TreeNode convertPrefixExpr(JCTree.JCUnary node) {
    return new PrefixExpression()
        .setTypeMirror(node.type)
        .setOperator(PrefixExpression.Operator.parse(node.getOperator().name.toString()))
        .setOperand((Expression) convert(node.getExpression()));
  }

  private TreeNode convertParens(JCTree.JCParens node) {
    return new ParenthesizedExpression()
        .setExpression((Expression) convert(node.getExpression()));
  }

  private TreeNode convertPostExpr(JCTree.JCUnary node) {
    return new PostfixExpression()
        .setOperator(PostfixExpression.Operator.parse(node.getOperator().name.toString()))
        .setOperand((Expression) convert(node.getExpression()));
  }

  private TreeNode convertPrimitiveType(JCTree.JCPrimitiveTypeTree node) {
    return new PrimitiveType(node.type);
  }

  private TreeNode convertReturn(JCTree.JCReturn node) {
    return new ReturnStatement((Expression) convert(node.getExpression()));
  }

  private TreeNode convertStringLiteral(JCTree.JCLiteral node) {
    return new StringLiteral((String) node.getValue(), node.type);
  }

  private TreeNode convertSwitch(JCTree.JCSwitch node) {
    SwitchStatement newNode = new SwitchStatement()
        .setExpression(convertWithoutParens(node.getExpression()));
    for (JCTree.JCCase switchCase : node.getCases()) {
      newNode.addStatement((SwitchCase) convert(switchCase));
      for (JCTree.JCStatement s : switchCase.getStatements()) {
        newNode.addStatement((Statement) convert(s));
      }
    }
    return newNode;
  }

  private TreeNode convertSynchronized(JCTree.JCSynchronized node) {
    return new SynchronizedStatement()
        .setExpression((Expression) convert(node.getExpression()))
        .setBody((Block) convertBlock(node.getBlock()));
  }

  private TreeNode convertThrow(JCTree.JCThrow node) {
    return new ThrowStatement()
        .setExpression((Expression) convert(node.getExpression()));
  }

  private TreeNode convertTry(JCTree.JCTry node) {
    TryStatement newNode = new TryStatement();
    for (Object obj : node.getResources()) {
      newNode.addResource(convertVariableExpression((JCTree.JCVariableDecl) obj));
    }
    for (Object obj : node.getCatches()) {
      newNode.addCatchClause((CatchClause) convert(obj));
    }
    return newNode
        .setBody((Block) convert(node.getBlock()))
        .setFinally((Block) convert(node.getFinallyBlock()));
  }

  private TreeNode convertType(JCTree.JCExpression node, Type newType) {
    return newType
        .setTypeMirror(node.type);
  }

  private TypeMirror nameType(JCTree node) {
    if (node.getKind() == Kind.PARAMETERIZED_TYPE) {
      return ((JCTree.JCTypeApply) node).clazz.type;
    }
    if (node.getKind() == Kind.ARRAY_TYPE) {
      return ((JCTree.JCArrayTypeTree) node).type;
    }
    return nameSymbol(node).asType();
  }

  private Symbol nameSymbol(JCTree node) {
    return node.getKind() == Kind.MEMBER_SELECT
        ? ((JCTree.JCFieldAccess) node).sym
        : ((JCTree.JCIdent) node).sym;
  }

  private TreeNode convertTypeApply(JCTree.JCTypeApply node) {
    return new ParameterizedType()
        .setType(Type.newType(node.type))
        .setTypeMirror(node.type);
  }

  private TreeNode convertTypeCast(JCTree.JCTypeCast node) {
    return new CastExpression(node.type, (Expression) convert(node.getExpression()));
  }

  private TreeNode convertVariableDeclaration(JCTree.JCVariableDecl node) {
    VarSymbol var = node.sym;
    SourcePosition pos = getPosition(node);
    if (var.getKind() == ElementKind.FIELD) {
      return new FieldDeclaration(var, (Expression) convert(node.getInitializer()));
    }
    if (var.getKind() == ElementKind.LOCAL_VARIABLE) {
      return new VariableDeclarationStatement(var, (Expression) convert(node.getInitializer()))
          .setType(convertType(var.asType(), pos, false));
    }
    if (var.getKind() == ElementKind.ENUM_CONSTANT) {
      EnumConstantDeclaration newNode = new EnumConstantDeclaration()
          .setName(convertSimpleName(var, node.type, getNamePosition(node)))
          .setVariableElement(var);
      ClassInstanceCreation init = (ClassInstanceCreation) convert(node.getInitializer());
      TreeUtil.copyList(init.getArguments(), newNode.getArguments());
      if (init.getAnonymousClassDeclaration() != null) {
        newNode.setAnonymousClassDeclaration(TreeUtil.remove(init.getAnonymousClassDeclaration()));
      }
      return newNode
          .setExecutablePair(init.getExecutablePair());
    }
    return convertSingleVariable(node);
  }

  private TreeNode convertSingleVariable(JCTree.JCVariableDecl node) {
    VarSymbol var = node.sym;
    SourcePosition pos = getPosition(node);
    boolean isVarargs = (node.sym.flags() & Flags.VARARGS) > 0;
    Type newType = convertType(var.asType(), pos, isVarargs);
    return new SingleVariableDeclaration()
        .setType(newType)
        .setIsVarargs(isVarargs)
        .setName(convertSimpleName(var, node.type, getNamePosition(node)))
        .setVariableElement(var)
        .setInitializer((Expression) convert(node.getInitializer()));
  }

  private Type convertType(TypeMirror varType, SourcePosition pos, boolean isVarargs) {
    Type newType;
    if (isVarargs) {
      newType = Type.newType(((javax.lang.model.type.ArrayType) varType).getComponentType());
    } else {
      if (varType.getKind() == TypeKind.DECLARED
          && !((DeclaredType) varType).getTypeArguments().isEmpty()) {
        newType = new ParameterizedType()
            .setType((SimpleType) new SimpleType(varType).setPosition(pos))
            .setTypeMirror(varType);
      } else {
        newType = Type.newType(varType);
      }
    }
    return (Type) newType.setPosition(pos);
  }

  private VariableDeclarationExpression convertVariableExpression(JCTree.JCVariableDecl node) {
    VarSymbol var = node.sym;
    boolean isVarargs = (node.sym.flags() & Flags.VARARGS) > 0;
    Type newType = convertType(var.asType(), getPosition(node), isVarargs);
    VariableDeclarationFragment fragment = new VariableDeclarationFragment();
    fragment
        .setName(convertSimpleName(var, node.type, getNamePosition(node)))
        .setVariableElement(var)
        .setInitializer((Expression) convert(node.getInitializer()));
    return new VariableDeclarationExpression()
        .setType(newType)
        .addFragment(fragment);
  }

  private TreeNode convertWhileLoop(JCTree.JCWhileLoop node) {
    return new WhileStatement()
        .setExpression(convertWithoutParens(node.getCondition()))
        .setBody((Statement) convert(node.getStatement()));
  }

  private TreeNode getAssociatedJavaDoc(JCTree node) {
    Comment comment = convertAssociatedComment(node);
    return comment != null && comment.isDocComment() ? comment : null;
  }

  private Comment convertAssociatedComment(JCTree node) {
    DocCommentTable docComments = unit.docComments;
    if (docComments == null || !docComments.hasComment(node)) {
      return null;
    }
    com.sun.tools.javac.parser.Tokens.Comment javacComment = docComments.getComment(node);
    Comment comment;
    switch (javacComment.getStyle()) {
      case BLOCK:
        comment = new BlockComment();
        break;
      case JAVADOC:
        comment = new Javadoc();
        break;
      case LINE:
        comment = new LineComment();
        break;
      default:
        throw new AssertionError("unknown comment type");
    }
    int startPos = javacComment.getSourcePos(0);
    int endPos = startPos + javacComment.getText().length();
    comment.setSourceRange(startPos, endPos);
    return comment;
  }

  private static void addOcniComments(CompilationUnit unit) {
    // Can't use a regex because it will greedily include everything between
    // the first and last closing pattern, resulting in a single comment node.
    String source = unit.getSource();
    int startPos = 0;
    int endPos = 0;
    while ((startPos = source.indexOf("/*-[", endPos)) > -1) {
      endPos = source.indexOf("]-*/", startPos);
      if (endPos > startPos) {
        endPos += 4;  // Include closing delimiter.
        BlockComment ocniComment = new BlockComment();
        ocniComment.setSourceRange(startPos, endPos);
        unit.getCommentList().add(ocniComment);
      }
    }
  }

  private static String getPath(JavaFileObject file) {
    String uri = file.toUri().toString();
    if (uri.startsWith("mem:/")) {
      // MemoryFileObject needs a custom file system for URI to return the
      // correct path, so the URI string is split instead.
      return uri.substring(5);
    }
    return file.toUri().getPath();
  }

  private String getTreeSource(JCTree node) {
    try {
      CharSequence source = unit.getSourceFile().getCharContent(true);
      return source.subSequence(node.getStartPosition(), node.getEndPosition(unit.endPositions))
          .toString();
    } catch (IOException e) {
      return node.toString();
    }
  }

  // javac uses a JCParens for the if, do, and while statements, while JDT doesn't.
  private Expression convertWithoutParens(JCTree.JCExpression condition) {
    Expression result = (Expression) convert(condition);
    if (result.getKind() == TreeNode.Kind.PARENTHESIZED_EXPRESSION) {
      result = TreeUtil.remove(((ParenthesizedExpression) result).getExpression());
    }
    return result;
  }

  private SourcePosition getSourcePosition(int start, int end) {
    if (unit.getLineMap() != null) {
      int line = unit.getLineMap().getLineNumber(start);
      return new SourcePosition(start, end, line);
    } else {
      return new SourcePosition(start, end);
    }
  }

  // Return best guess for the position of a declaration node's name.
  private SourcePosition getNamePosition(JCTree node) {
    if (node.pos == -1) {
      return SourcePosition.NO_POSITION;
    }
    String src = newUnit.getSource();
    int start = node.pos;
    Kind kind = node.getKind();
    if (kind == Kind.ANNOTATION_TYPE || kind == Kind.CLASS
        || kind == Kind.ENUM || kind == Kind.INTERFACE) {
      // Skip the class/enum/interface token.
      while (src.charAt(start++) != ' ') {
      }
    } else if (kind != Kind.METHOD && kind != Kind.VARIABLE) {
      return getPosition(node);
    }
    if (!Character.isJavaIdentifierStart(src.charAt(start))) {
      return getPosition(node);
    }
    int endPos = start + 1;
    while (Character.isJavaIdentifierPart(src.charAt(endPos))) {
      endPos++;
    }
    return getSourcePosition(start, endPos);
  }

  // Helper class for convertBinary().
  private static class StackState {
    private final JCTree.JCBinary expression;
    private int nextChild = -2;

    private StackState(JCTree.JCBinary expr) {
      expression = expr;
    }

    private JCTree.JCExpression nextChild() {
      int childIdx = nextChild++;
      if (childIdx == -2) {
        return expression.getLeftOperand();
      } else if (childIdx == -1) {
        return expression.getRightOperand();
      } else {
        return null;
      }
    }
  }
}
